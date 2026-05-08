package org.example.expression;

import org.example.core.Environment;
import org.example.exception.MiniException;

/*
 * A BinaryExpression osztály az Absztrakt Szintaxisfa (AST)
 * egyik leggyakoribb csomópontja.
 * Két részkifejezésből (bal és jobb oldal) és egy operátorból (+, -, *, /) áll.
 * Ez az osztály felel a dinamikus típusellenőrzésért
 * és az operátorok polimorfikus viselkedéséért
 * (pl. a '+' jel matematikát és szövegösszefűzést is végezhet).
 */
public class BinaryExpression implements Expression {

    private Expression left;
    private Expression right;
    private char operator;

    public BinaryExpression(Expression left, char operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }


    @Override
    public Object evaluate(Environment env) {
        // 1. Rekurzív kiértékelés
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Szövegösszefűzés
        if (operator == '+') {
            if (leftVal instanceof String || rightVal instanceof String) {
                return String.valueOf(leftVal) + String.valueOf(rightVal);
            }
        }

        // Szigorú típusellenőrzés
        if (!(leftVal instanceof Double) || !(rightVal instanceof Double)) {
            throw new MiniException("Matematikai muvelet ('" + operator +
                    "') csak szamokon vegezheto el. Talalt tipusok: " +
                    leftVal.getClass().getSimpleName() + " es " +
                    rightVal.getClass().getSimpleName());
        }

        // Biztonságos kasztolás
        double leftNum = (Double) leftVal;
        double rightNum = (Double) rightVal;

        // Művelet elvégzése
        switch (operator) {
            case '+': return leftNum + rightNum;
            case '-': return leftNum - rightNum;
            case '*': return leftNum * rightNum;
            case '/':
                if (rightNum == 0) {
                    throw new MiniException("Nullaval osztas!");
                }
                return leftNum / rightNum;
            default:
                throw new MiniException("Ismeretlen operator: " + operator);
        }
    }
}