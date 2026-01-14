package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // Kifejezés Elemző Rész
    private List<String> tokens;
    private int currentTokenIndex;

    private List<String> tokenizeExpression(String exprStr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean inString = false;

        // Operátorok és speciális karakterek
        String ops = "==|!=|<=|>=|&&|\\|\\||[<>+\\-*/()=]";
        Pattern opPattern = Pattern.compile("^(" + ops + ")");

        for (int i = 0; i < exprStr.length(); i++) {
            char c = exprStr.charAt(i);

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
                    if (currentToken.length() > 0) {
                        tokens.add(currentToken.toString());
                        currentToken.setLength(0);
                    }
                } else {
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

    private Expression parseExpression(String exprStr) {
        this.tokens = tokenizeExpression(exprStr);
        this.currentTokenIndex = 0;
        return parseComparison();
    }

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

    private Expression parsePrimary() {
        if (currentTokenIndex >= tokens.size()) {
            throw new RuntimeException("Váratlan vége a kifejezésnek");
        }
        String token = tokens.get(currentTokenIndex);
        currentTokenIndex++;

        if (token.equals("(")) {
            Expression expr = parseComparison();
            if (currentTokenIndex >= tokens.size() || !tokens.get(currentTokenIndex).equals(")")) {
                throw new RuntimeException("Szintaktikai hiba: Hiányzó ')'");
            }
            currentTokenIndex++;
            return expr;
        } else if (token.startsWith("\"") && token.endsWith("\"")) {
            String strValue = token.substring(1, token.length() - 1);
            return new StringExpression(strValue);
        } else if (isNumeric(token)) {
            return new NumberExpression(Double.parseDouble(token));
        } else {
            return new VariableExpression(token);
        }
    }

    private static boolean isNumeric(String str) {
        if (str == null) return false;
        try { Double.parseDouble(str); return true; }
        catch (NumberFormatException e) { return false; }
    }

    // --- Parancs Elemző Rész ---

    private List<String> lines;
    private int currentLineIndex;

    public List<Statement> parse(String sourceCode) {
        this.lines = Arrays.asList(sourceCode.split("\n"));
        this.currentLineIndex = 0;
        return parseStatementBlock();
    }

    private List<Statement> parseStatementBlock(String... terminators) {
        List<Statement> statements = new ArrayList<>();

        while (currentLineIndex < lines.size()) {
            String line = lines.get(currentLineIndex).trim();

            // Kommentek levágása
            int commentIndex = line.indexOf("#");
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex).trim();
            }

            // Terminátor figyelése (pl. "}" vagy "} else {")
            if (terminators != null && terminators.length > 0) {
                for (String terminator : terminators) {
                    if (line.equals(terminator)) {
                        return statements;
                    }
                }
            }

            currentLineIndex++;

            if (line.isEmpty()) {
                continue;
            }

            // --- Itt dől el, milyen parancs jön ---
            if (line.startsWith("if")) {
                statements.add(parseIfStatement(line));
            }
            else if (line.startsWith("while")) {
                statements.add(parseWhileStatement(line));
            }
            // ÚJ: A FOR ciklus felismerése
            else if (line.startsWith("for")) {
                statements.add(parseForStatement(line));
            }
            else {
                statements.add(parseSimpleStatement(line));
            }
        }

        if (terminators != null && terminators.length > 0) {
            throw new RuntimeException("Szintaktikai hiba: Hiányzó lezárás: " + String.join(" vagy ", terminators));
        }

        return statements;
    }

    private Statement parseIfStatement(String line) {
        // Kapcsos zárójelet vár a végén
        Pattern ifPattern = Pattern.compile("^if\\s*\\((.*)\\)\\s*\\{$");
        Matcher matcher = ifPattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'if' szintaxis: '" + line + "'. Várt formátum: if (feltétel) {");
        }

        String conditionString = matcher.group(1);
        Expression condition = parseExpression(conditionString);

        List<Statement> thenBlock = parseStatementBlock("}", "} else {");
        List<Statement> elseBlock = null;

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

    private Statement parseWhileStatement(String line) {
        Pattern whilePattern = Pattern.compile("^while\\s*\\((.*)\\)\\s*\\{$");
        Matcher matcher = whilePattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'while' szintaxis: '" + line + "'. Várt formátum: while (feltétel) {");
        }

        String conditionString = matcher.group(1);
        Expression condition = parseExpression(conditionString);

        List<Statement> body = parseStatementBlock("}");

        currentLineIndex++;

        return new WhileStatement(condition, body);
    }

    // ÚJ: A FOR ciklus elemző metódusa
    private Statement parseForStatement(String line) {
        // Regex: for ( ... ) {
        Pattern forPattern = Pattern.compile("^for\\s*\\((.*)\\)\\s*\\{$");
        Matcher matcher = forPattern.matcher(line);

        if (!matcher.matches()) {
            throw new RuntimeException("Hibás 'for' szintaxis: '" + line + "'. Várt formátum: for (init; feltétel; léptetés) {");
        }

        String content = matcher.group(1);
        String[] parts = content.split(";");

        if (parts.length != 3) {
            throw new RuntimeException("A 'for' ciklusnak pontosan 3 része kell legyen (init; feltétel; léptetés) ; elválasztva!");
        }

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

    private Statement parseSimpleStatement(String line) {
        String[] parts = line.split("\\s+");

        if (parts[0].equals("var") && parts.length >= 4 && parts[2].equals("=")) {
            String varName = parts[1];
            String exprStr = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
            return new VarAssignStatement(varName, parseExpression(exprStr));

        } else if (parts[0].equals("print")) {
            String exprStr = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            return new PrintStatement(parseExpression(exprStr));

        } else if (parts.length >= 3 && parts[1].equals("=")) {
            String varName = parts[0];
            String exprStr = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
            return new VarAssignStatement(varName, parseExpression(exprStr));

        } else {
            throw new RuntimeException("Szintaktikai hiba: Ismeretlen parancs '" + line + "'");
        }
    }
}