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
    private String operator;
    private Expression right;

    public ComparisonExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


    @Override
    public Object evaluate(Environment env) {
        // A két oldal rekurzív kiértékelése
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);
        boolean result;

        // Típusellenőrzés és műveletvégzés (esetek)
        // 1. Mindkét oldal Szöveg (String)
        if (leftVal instanceof String && rightVal instanceof String) {
            String leftStr = (String) leftVal;
            String rightStr = (String) rightVal;
            switch (operator) {
                case "==": result = leftStr.equals(rightStr); break;
                case "!=": result = !leftStr.equals(rightStr); break;
                default:
                    throw new RuntimeException("Az operátor ('" + operator +
                            "') nem használható stringekre.");
            }
        }
        // 2. Mindkét oldal Szám (Double)
        else if (leftVal instanceof Double && rightVal instanceof Double) {
            double leftNum = (Double) leftVal;
            double rightNum = (Double) rightVal;
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
        // 3. Típusok keveredése
        else {
            switch (operator) {
                case "==": result = false; break;
                case "!=": result = true; break;
                default:
                    throw new RuntimeException("Nem lehet összehasonlítani különböző típusokat: " +
                            leftVal.getClass().getSimpleName() + " és " +
                            rightVal.getClass().getSimpleName());
            }
        }

        // Visszatérési érték
        return result ? 1.0 : 0.0;
    }
}