package org.example.expression;

import org.example.core.Environment;

/**
 * A LogicalExpression (Logikai Kifejezés) osztály
 * felelős az összetett logikai műveletek (&&, ||)
 * végrehajtásáért.
 * Ez az osztály két fontos paradigmát valósít meg:
 * 1. A Rövidzár-kiértékelést (Short-circuit evaluation),
 * ami optimalizálja a futásidőt.
 * 2. A "Truthy / Falsy" koncepciót, amely a dinamikus
 * típusokat automatikusan logikai igazzá vagy hamissá konvertálja.
 */
public class LogicalExpression implements Expression {
    private Expression left;
    private String operator;
    private Expression right;

    public LogicalExpression(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


    @Override
    public Object evaluate(Environment env) {

        Object leftVal = left.evaluate(env);

        boolean leftBool = isTruthy(leftVal);

        if (operator.equals("||")) {

            if (leftBool) return 1.0;
            return isTruthy(right.evaluate(env)) ? 1.0 : 0.0;

        } else if (operator.equals("&&")) {
            if (!leftBool) return 0.0;

            return isTruthy(right.evaluate(env)) ? 1.0 : 0.0;
        }

        throw new RuntimeException("Ismeretlen logikai operátor: " + operator);
    }


    private boolean isTruthy(Object obj) {

        if (obj instanceof Double) {
            return (Double) obj != 0.0;
        }
        if (obj instanceof String) {
            return !((String) obj).isEmpty();
        }

        return obj != null;
    }
}