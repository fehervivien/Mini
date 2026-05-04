package org.example.expression;

import org.example.core.Environment;

/*
 * A ComparisonExpression (Összehasonlító Kifejezés)
 * felel a relációs operátorok (==, !=, <, <=, >, >=)
 * kiértékeléséért.
 * Ennek az AST csomópontnak az a feladata,
 * hogy eldöntse két értékről a relációjukat,
 * és az eredményt a Mini nyelv saját logikai
 * reprezentációjába (1.0 = Igaz, 0.0 = Hamis)
 * csomagolva adja vissza.
 */
public class ComparisonExpression implements Expression {

    private Expression left;
    // Az operátor szöveges formában, pl. "==" vagy ">="
    private String operator;
    private Expression right;

    public ComparisonExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /*
     * Elvégzi az összehasonlítást a bal és jobb
     * oldali kifejezések futásidejű értékein.
     * @param env: Az aktuális futási környezet.
     * @return 1.0 ha a feltétel igaz, 0.0 ha hamis.
     */
    @Override
    public Object evaluate(Environment env) {
        // A két oldal rekurzív kiértékelése
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // A Java saját logikai típusa,
        // ami a végén átalakul
        boolean result;

        // TÍPUSELLENŐRZÉS ÉS MŰVELETVÉGZÉS
        // 1. ESET: Mindkét oldal Szöveg (String)
        if (leftVal instanceof String && rightVal instanceof String) {
            String leftStr = (String) leftVal;
            String rightStr = (String) rightVal;
            switch (operator) {
                // Szövegek esetén csak az azonosságot
                // vagy a különbözőséget vizsgálhatja
                case "==": result = leftStr.equals(rightStr); break;
                case "!=": result = !leftStr.equals(rightStr); break;
                default:
                    // pl. "alma" > "korte" nem értelmezett a nyelvben
                    throw new RuntimeException("Az operátor ('" + operator +
                            "') nem használható stringekre.");
            }
        }
        // 2. ESET: Mindkét oldal Szám (Double)
        else if (leftVal instanceof Double && rightVal instanceof Double) {
            double leftNum = (Double) leftVal;
            double rightNum = (Double) rightVal;
            // Számoknál minden matematikai reláció értelmezett
            switch (operator) {
                case "==": result = leftNum == rightNum; break;
                case "!=": result = leftNum != rightNum; break;
                case "<":  result = leftNum < rightNum; break;
                case "<=": result = leftNum <= rightNum; break;
                case ">":  result = leftNum > rightNum; break;
                case ">=": result = leftNum >= rightNum; break;
                default:
                    throw new RuntimeException("Ismeretlen operátor: " + operator);
            }
        }
        // 3. ESET: Típusok keveredése
        // (pl. String hasonlítása Double-höz)
        else {
            switch (operator) {
                // Ha egyenlőséget vizsgálnak két különböző
                // típus között, az logikailag mindig hamis.
                // Ez megvédi a programot az összeomlástól,
                // és a Python/JS működéséhez hasonlít.
                case "==": result = false; break;
                case "!=": result = true; break;
                default:
                    // Irányított reláció (pl. "alma" < 5)
                    // esetén azonnali hibát dob,
                    // mert ez logikai bukfenc
                    // (Type mismatch).
                    throw new RuntimeException("Nem lehet összehasonlítani különböző típusokat: " +
                            leftVal.getClass().getSimpleName() + " és " +
                            rightVal.getClass().getSimpleName());
            }
        }

        // VISSZATÉRÉSI ÉRTÉK KONVERZIÓJA
        // (C-style Truthiness)

        // A Mini nyelv nem rendelkezik natív
        // boolean típussal, ezért a Java 'true' értékét
        // 1.0-ra, a 'false' értékét 0.0-ra konvertáljuk.
        return result ? 1.0 : 0.0;
    }
}