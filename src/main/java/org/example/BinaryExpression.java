package org.example;

/**
 * Egy bináris műveletet reprezentál.
 * MÓDOSÍTÁS: Ez az osztály most már típusellenőrzést végez.
 * A '+' operátor string-összefűzésre is képes.
 * A többi operátor (-, *, /) csak számokon működik.
 */
public class BinaryExpression implements Expression {
    private Expression left;
    private Expression right;
    private char operator; // '+', '-', '*', '/'

    public BinaryExpression(Expression left, char operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // --- Speciális eset: String összefűzés a '+' operátorral ---
        if (operator == '+') {
            // Ha bármelyik oldal String, az egész művelet string összefűzés lesz
            if (leftVal instanceof String || rightVal instanceof String) {
                return String.valueOf(leftVal) + String.valueOf(rightVal);
            }
        }

        // --- Minden más eset: Matematikai műveletek ---
        // Elvárjuk, hogy mindkét operandus szám (Double) legyen

        if (!(leftVal instanceof Double) || !(rightVal instanceof Double)) {
            throw new RuntimeException("Matematikai művelet ('" + operator +
                    "') csak számokon végezhető el. Talált típusok: " +
                    leftVal.getClass().getSimpleName() + " és " +
                    rightVal.getClass().getSimpleName());
        }

        // Kasztolás (tudjuk, hogy Double-k)
        double leftNum = (Double) leftVal;
        double rightNum = (Double) rightVal;

        switch (operator) {
            case '+': return leftNum + rightNum; // Ezt a részt már csak számok érhetik el
            case '-': return leftNum - rightNum;
            case '*': return leftNum * rightNum;
            case '/':
                if (rightNum == 0) {
                    throw new RuntimeException("Nullával osztás!");
                }
                return leftNum / rightNum;
            default:
                throw new RuntimeException("Ismeretlen operátor: " + operator);
        }
    }
}