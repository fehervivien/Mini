package org.example.core;

import org.example.expression.*;
import org.example.statement.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * A Parser (Szintaktikai Elemző) osztály felelős a forráskód értelmezéséért.
 * Két fő feladatot lát el:
 * 1. Lexikális elemzés (Tokenizálás):
 *   A karaktersorozatokat értelmes szavakká (tokenekké) bontja.
 * 2. Szintaktikai elemzés:
 *   A tokenekből Absztrakt Szintaxisfát (AST) épít
 *   a nyelvtani szabályok alapján.
 * A megvalósítás egy "Recursive Descent Parser" (Rekurzív leszálló elemző).
 */

public class Parser {

    // 1. RÉSZ: KIFEJEZÉS ELEMZŐ (EXPRESSION PARSER)
    private List<String> tokens;
    private int currentTokenIndex;

    // Jelzi, ha a sorokon átívelő komment még tart
    private boolean inMultiLineComment = false;

    /* Lexikális elemző a kifejezésekhez.
     * Egy matematikai vagy logikai sort darabol fel
     * alapegységekre (tokenekre):
     * ["x", "+", "5", "==", "10"].
     */
    private List<String> tokenizeExpression(String exprStr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inString = false;


        // Reguláris kifejezés az operátorok
        // és speciális elválasztók felismerésére
        String ops = "==|!=|<=|>=|&&|\\|\\||[<>+\\-*/()=,]";
        Pattern opPattern = Pattern.compile("^(" + ops + ")");

        for (int i = 0; i < exprStr.length(); i++) {
            char c = exprStr.charAt(i);

            // Szöveges literálok ("idézőjeles részek")
            // egyben tartása
            if (inString) {
                if (c == '"') {
                    currentToken.append(c);
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                    inString = false;
                } else {
                    currentToken.append(c);
                }
            } else {
                if (c == '"') {
                    inString = true;
                    currentToken.append(c);
                } else if (Character.isWhitespace(c)) {
                    // Szóközök mentén darabolja
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                    }
                } else {
                    // Operátorok leválasztása a szavakról
                    Matcher matcher = opPattern.matcher(exprStr.substring(i));
                    if (matcher.find()) {
                        if (currentToken.length() > 0) {
                            tokens.add(currentToken.toString());
                            currentToken.setLength(0);
                        }
                        String op = matcher.group(1);
                        tokens.add(op);
                        i += op.length() - 1;
                    } else {
                        currentToken.append(c);
                    }
                }
            }
        }
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        return tokens;
    }

    /* A kifejezés elemzés belépési pontja.
     * Innentől kezdve a függvények hívási lánca
     * határozza meg a műveleti sorrendet (Precedencia).
     * A leggyengébb operátortól haladunk a legerősebb felé.
     */
    private Expression parseExpression(String exprStr) {
        this.tokens = tokenizeExpression(exprStr);
        this.currentTokenIndex = 0;
        return parseLogicalOr();
    }

    // VAGY (||) operátor feldolgozása (Leggyengébb prioritású)
    private Expression parseLogicalOr() {
        Expression left = parseLogicalAnd();
        while (currentTokenIndex < tokens.size()) {
            String token = tokens.get(currentTokenIndex);
            if (token.equals("||")) {
                currentTokenIndex++;
                Expression right = parseLogicalAnd();
                left = new LogicalExpression(left, token, right);
            } else {
                break;
            }
        }
        return left;
    }

    // ÉS (&&) operátor feldolgozása
    private Expression parseLogicalAnd() {
        Expression left = parseComparison();
        while (currentTokenIndex < tokens.size()) {
            String token = tokens.get(currentTokenIndex);
            if (token.equals("&&")) {
                currentTokenIndex++;
                Expression right = parseComparison();
                left = new LogicalExpression(left, token, right);
            } else {
                break;
            }
        }
        return left;
    }

    // Relációs operátorok (==, !=, <, >) feldolgozása
    private Expression parseComparison() {
        Expression left = parseAdditionSubtraction();
        while (currentTokenIndex < tokens.size()) {
            String token = tokens.get(currentTokenIndex);
            if (isComparisonOperator(token)) {
                currentTokenIndex++;
                Expression right = parseAdditionSubtraction();
                left = new ComparisonExpression(left, token, right);
            } else {
                break;
            }
        }
        return left;
    }

    private boolean isComparisonOperator(String token) {
        return token.equals("==") || token.equals("!=") || token.equals("<") ||
                token.equals("<=") || token.equals(">") || token.equals(">=");
    }

    // Összeadás és Kivonás (+, -)
    private Expression parseAdditionSubtraction() {
        Expression left = parseMultiplicationDivision();
        while (currentTokenIndex < tokens.size()) {
            String token = tokens.get(currentTokenIndex);
            if (token.equals("+")) {
                currentTokenIndex++;
                Expression right = parseMultiplicationDivision();
                left = new BinaryExpression(left, '+', right);
            } else if (token.equals("-")) {
                currentTokenIndex++;
                Expression right = parseMultiplicationDivision();
                left = new BinaryExpression(left, '-', right);
            } else {
                break;
            }
        }
        return left;
    }

    // Szorzás és Osztás (*, /)
    private Expression parseMultiplicationDivision() {
        Expression left = parsePrimary();
        while (currentTokenIndex < tokens.size()) {
            String token = tokens.get(currentTokenIndex);
            if (token.equals("*")) {
                currentTokenIndex++;
                Expression right = parsePrimary();
                left = new BinaryExpression(left, '*', right);
            } else if (token.equals("/")) {
                currentTokenIndex++;
                Expression right = parsePrimary();
                left = new BinaryExpression(left, '/', right);
            } else {
                break;
            }
        }
        return left;
    }

    /**
     * A legmagasabb prioritású elemek feldolgozása.
     * Itt dől el, hogy egy token mi:
     * szám, szöveg, zárójelbe tett kifejezés,
     * vagy függvényhívás.
     */
    private Expression parsePrimary() {
        if (currentTokenIndex >= tokens.size()) {
            throw new RuntimeException("Váratlan vége a kifejezésnek");
        }
        String token = tokens.get(currentTokenIndex);
        currentTokenIndex++;

        // Zárójelezett kifejezés
        // (a zárójel felülbírálja a műveleti sorrendet)
        if (token.equals("(")) {
            Expression expr = parseComparison();
            if (currentTokenIndex >= tokens.size() || !tokens.get(currentTokenIndex).equals(")")) {
                throw new RuntimeException("Szintaktikai hiba: Hiányzó ')'");
            }
            currentTokenIndex++;
            return expr;

          // Konzol bemenet olvasása
        } else if (token.equals("input")) {
            return new InputExpression();

          // Szöveges (String) literál felismerése
        } else if (token.startsWith("\"") && token.endsWith("\"")) {
            String strValue = token.substring(1, token.length() - 1);
            return new StringExpression(strValue);

          // Szám felismerése
        } else if (isNumeric(token)) {
            return new NumberExpression(Double.parseDouble(token));

          // Egyéb esetben: Változónév VAGY Függvényhívás
        } else {
            String name = token;

            // Ha a nevet egy nyitó zárójel '(' követi,
            // akkor ez egy függvényhívás!
            if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals("(")) {
                currentTokenIndex++;

                List<Expression> args = new ArrayList<>();
                if (!tokens.get(currentTokenIndex).equals(")")) {
                    // Első argumentum kiértékelése
                    args.add(parseLogicalOr());

                    // Ha vessző jön, akkor több argumentum is van
                    while (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(",")) {
                        // ',' átlépése
                        currentTokenIndex++;
                        args.add(parseLogicalOr());
                    }
                }

                if (currentTokenIndex >= tokens.size() || !tokens.get(currentTokenIndex).equals(")")) {
                    throw new RuntimeException("Hianyzik a ')' a fuggveny utan: " + name);
                }
                currentTokenIndex++; // ')' átlépése
                return new CallExpression(name, args);
            }

            // Ha nem volt utána zárójel,
            // akkor ez csak egy sima változó olvasása
            return new VariableExpression(name);
        }
    }

    // Segédfüggvény: Eldönti egy stringről, hogy valós szám-e
    private static boolean isNumeric(String str) {
        if (str == null) return false;
        try { Double.parseDouble(str); return true; }
        catch (NumberFormatException e) { return false; }
    }

    // 2. RÉSZ: PARANCS ELEMZŐ (STATEMENT PARSER)
    private List<String> lines;
    private int currentLineIndex;

    /* A szintaktikai elemzés fő belépési pontja.
     * A forráskódot sorokra bontja, és elindítja
     * a blokk-szintű feldolgozást.
     */
    public List<Statement> parse(String sourceCode) {
        this.lines = Arrays.asList(sourceCode.split("\n"));
        this.currentLineIndex = 0;
        return parseStatementBlock();
    }

    /* Egy kódrészlet (blokk) elemzése, amíg el nem ér
     * egy lezáró szót (terminátort), pl. "}" vagy "} else {".
     * Ezzel oldja meg az egymásba ágyazott blokkok
     * (scope-ok) felépítését.
     */
    private List<Statement> parseStatementBlock(String... terminators) {
        List<Statement> statements = new ArrayList<>();

        while (currentLineIndex < lines.size()) {
            String line = lines.get(currentLineIndex).trim();

            // Kommentek levágása a sor végéről
            int commentIndex = line.indexOf("#");
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex).trim();
            }

            // Terminátor (blokk lezárás) figyelése
            // (pl. "}" vagy "} else {")
            if (terminators != null && terminators.length > 0) {
                for (String terminator : terminators) {
                    if (line.equals(terminator)) {
                        return statements;
                    }
                }
            }
            // Tovább lép a következő sorra
            currentLineIndex++;

            // Üres sorok átugrása
            if (line.isEmpty()) {
                continue;
            }

            // Sorszám elmentése a hibakezeléshez
            // (Decorator minta)
            int actualLineNumber = currentLineIndex;
            Statement stmt = null;

            // Kulcsszavak alapján eldönti,
            // hogy milyen parancsról van szó
            if (line.startsWith("if")) {
                stmt = parseIfStatement(line);
            }
            else if (line.startsWith("while")) {
                stmt = parseWhileStatement(line);
            }
            else if (line.startsWith("for")) {
                stmt = parseForStatement(line);
            }
            else if (line.startsWith("func ")) {
                stmt = parseFunctionStatement(line);
            }
            else if (line.startsWith("switch")) {
                stmt = parseSwitchStatement(line);
            }
            else {
                stmt = parseSimpleStatement(line);
            }

            // Becsomagolja az eredeti parancsot
            // a "nyomkövetőbe" a pontos hibaüzenetekért
            statements.add(new TrackedStatement(stmt, actualLineNumber));
        }

        // Ha a fájl véget ért, de nem találta meg
        // a várt lezáró jelet (pl. hiányzó '}')
        if (terminators != null && terminators.length > 0) {
            throw new RuntimeException("Szintaktikai hiba: Hiányzó lezárás: " + String.join(" vagy ", terminators));
        }

        return statements;
    }

    // IF elágazás elemzése (Zárójelek nélküli,
    // modern szintaxis támogatása)
    private Statement parseIfStatement(String line) {
        // Regex: if kulcsszó, utána
        // a feltétel, majd a blokknyitó {
        Pattern ifPattern = Pattern.compile("^if\\s+(.*?)\\s*\\{$");
        Matcher matcher = ifPattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'if' szintaxis: '" + line + "'. Várt formátum (zárójelek nélkül): if feltétel {");
        }

        String conditionString = matcher.group(1);
        Expression condition = parseExpression(conditionString);

        // A 'then' ág beolvasása, amíg el nem éri
        // a sima zárást "}" vagy az "} else {" ágat
        List<Statement> thenBlock = parseStatementBlock("}", "} else {");
        List<Statement> elseBlock = null;

        // Az else ág logikájának ellenőrzése
        if (currentLineIndex < lines.size()) {
            String stoppingLine = lines.get(currentLineIndex).trim();
            if (stoppingLine.contains("#")) stoppingLine = stoppingLine.substring(0, stoppingLine.indexOf("#")).trim();

            if (stoppingLine.equals("}")) {
                currentLineIndex++;
                if (currentLineIndex < lines.size()) {
                    String nextLine = lines.get(currentLineIndex).trim();
                    if (nextLine.contains("#")) nextLine = nextLine.substring(0, nextLine.indexOf("#")).trim();

                    if (nextLine.equals("else {")) {
                        currentLineIndex++;
                        elseBlock = parseStatementBlock("}");
                        currentLineIndex++;
                    }
                }
            }
            else if (stoppingLine.equals("} else {")) {
                currentLineIndex++;
                elseBlock = parseStatementBlock("}");
                currentLineIndex++;
            }
        }

        return new IfStatement(condition, thenBlock, elseBlock);
    }

    // WHILE ciklus elemzése (Zárójelek nélkül)
    private Statement parseWhileStatement(String line) {
        Pattern whilePattern = Pattern.compile("^while\\s+(.*?)\\s*\\{$");
        Matcher matcher = whilePattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'while' szintaxis: '" + line + "'. Várt formátum (zárójelek nélkül): while feltétel {");
        }

        String conditionString = matcher.group(1);
        Expression condition = parseExpression(conditionString);

        List<Statement> body = parseStatementBlock("}");

        currentLineIndex++;

        return new WhileStatement(condition, body);
    }

    /* FOR ciklus elemzése
     * Várt formátum: for inicilizálás; feltétel; léptetés {
     */
    private Statement parseForStatement(String line) {
        Pattern forPattern = Pattern.compile("^for\\s+(.*?)\\s*\\{$");
        Matcher matcher = forPattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'for' szintaxis: '" + line + "'. Várt formátum (zárójelek nélkül): for init; feltétel; léptetés {");
        }

        String content = matcher.group(1);
        String[] parts = content.split(";");

        if (parts.length != 3) {
            throw new RuntimeException("A 'for' ciklusnak pontosan 3 része kell legyen (init; feltétel; léptetés) ; elválasztva!");
        }

        // A for ciklus 3 részre szedése és külön elemzése
        String initStr = parts[0].trim();
        String condStr = parts[1].trim();
        String stepStr = parts[2].trim();
        Statement initStmt = parseSimpleStatement(initStr);
        Expression conditionExpr = parseExpression(condStr);
        Statement stepStmt = parseSimpleStatement(stepStr);

        List<Statement> body = parseStatementBlock("}");

        currentLineIndex++;

        return new ForStatement(initStmt, conditionExpr, stepStmt, body);
    }

    /* Egyszerű, egy soros parancsok
     * (print, return, változó értékadás) felismerése.
     * Támogatja az implicit "var" elhagyást is.
     */
    private Statement parseSimpleStatement(String line) {
        // 1. Képernyőre írás (print)
        if (line.startsWith("print")) {
            String exprStr = line.substring(5).trim();
            return new PrintStatement(parseExpression(exprStr));
        }

        // 2. Visszatérés egy függvényből (return)
        if (line.startsWith("return")) {
            String exprStr = line.substring(6).trim();
            if (exprStr.isEmpty()) {
                // Sima "return" (érték nélkül)
                return new ReturnStatement(null);
            }
            return new ReturnStatement(parseExpression(exprStr)); // pl. "return x"
        }

        // 3. Változó értékadások
        String[] parts = line.split("\\s+");

        if (parts.length >= 3) {
            // Klasszikus, java-s megközelítés: "var x = 10"
            if (parts[0].equals("var") && parts[2].equals("=")) {
                String varName = parts[1];
                String exprStr = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
                return new VarAssignStatement(varName, parseExpression(exprStr));
            }
            // Modern, letisztult megközelítés: "x = 10"
            else if (parts[1].equals("=")) {
                String varName = parts[0];
                String exprStr = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                return new VarAssignStatement(varName, parseExpression(exprStr));
            }
        }

        throw new RuntimeException("Szintaktikai hiba: Ismeretlen parancs '" + line + "'");
    }

    /* Saját függvény definiálásának elemzése
     * (pl. func nev(arg1, arg2) { ... })
    */
    private Statement parseFunctionStatement(String line) {
        java.util.regex.Matcher m = Pattern.compile("^func\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\((.*)\\)\\s*\\{$").matcher(line);
        if (!m.matches()) throw new RuntimeException("Hibas 'func' szintaxis: " + line);

        String name = m.group(1);
        String[] args = m.group(2).split(",");

        // Paraméterlista feldarabolása és tisztítása
        List<String> params = new ArrayList<>();
        for (String a : args) {
            if (!a.trim().isEmpty()) params.add(a.trim());
        }

        List<Statement> body = parseStatementBlock("}");
        currentLineIndex++;
        return new FunctionStatement(name, params, body);
    }

    private Statement parseSwitchStatement(String line) {
        // 1. A 'switch' fejléc elemzése: switch kifejezés {
        Pattern switchPattern = Pattern.compile("^switch\\s+(.*?)\\s*\\{$");
        Matcher matcher = switchPattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'switch' szintaxis: " + line + ". Várt formátum: switch kifejezés {");
        }

        String conditionString = matcher.group(1);
        Expression condition = parseExpression(conditionString);

        // 2. Az ágak gyűjtése
        java.util.Map<Expression, Statement> cases = new java.util.LinkedHashMap<>();
        Statement defaultBranch = null;

        // Addig olvassa a sorokat, amíg el nem éri a switch végét jelző '}' jelet
        while (currentLineIndex < lines.size()) {
            String caseLine = lines.get(currentLineIndex).trim();

            // Ha elérte a switch lezárását
            if (caseLine.equals("}")) {
                currentLineIndex++;
                break;
            }

            if (caseLine.startsWith("case ")) {
                // Case ág: case érték {
                currentLineIndex++;
                Pattern casePattern = Pattern.compile("^case\\s+(.*?)\\s*\\{$");
                Matcher caseMatcher = casePattern.matcher(caseLine);

                if (!caseMatcher.matches()) throw new RuntimeException("Hibás 'case' szintaxis: " + caseLine);

                Expression caseValue = parseExpression(caseMatcher.group(1));
                // Beolvassa a hozzá tartozó blokkot a lezáró '}' jelig
                List<Statement> caseBody = parseStatementBlock("}");
                cases.put(caseValue, new BlockStatement(caseBody));
                currentLineIndex++;
            }
            else if (caseLine.startsWith("default {")) {
                currentLineIndex++;
                List<Statement> defaultBody = parseStatementBlock("}");
                defaultBranch = new BlockStatement(defaultBody);
                currentLineIndex++;
            }
            else if (caseLine.isEmpty()) {
                currentLineIndex++; // Üres sorok átugrása
            }
            else {
                throw new RuntimeException("Váratlan sor a switch blokkban: " + caseLine);
            }
        }

        return new SwitchStatement(condition, cases, defaultBranch);
    }

}