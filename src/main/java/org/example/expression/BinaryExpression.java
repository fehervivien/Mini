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

    // A faág bal és jobb oldali gyermeke
    private Expression left;
    private Expression right;

    // A végrehajtandó matematikai vagy szöveges művelet jele
    private char operator;


     // Konstruktor a bináris csomópont felépítéséhez.
    public BinaryExpression(Expression left, char operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /*
     * A kifejezés kiértékelése (futásidőben).
     * Rekurzívan kiértékeli a bal és jobb oldalt, majd az eredményeiken elvégzi a műveletet.
     * * @param env Az aktuális környezet (változók tárolója).
     * @return A művelet eredménye (Double vagy String).
     */
    @Override
    public Object evaluate(Environment env) {
        /* 1. Rekurzív kiértékelés:
           Először meg kell tudni a két oldal pontos értékét */
        Object leftVal = left.evaluate(env);
        Object rightVal = right.evaluate(env);

        // Speciális eset: Szövegösszefűzés (String Concatenation)
        if (operator == '+') {
            if (leftVal instanceof String || rightVal instanceof String) {
                return String.valueOf(leftVal) + String.valueOf(rightVal);
            }
        }

        // Szigorú típusellenőrzés (Strict Type Checking)
        // Ha idáig eljutott a kód, az azt jelenti, hogy matematikai
        // műveletet fog végezni.
        // Itt megköveteli, hogy mindkét oldal szám (Double) legyen.
        if (!(leftVal instanceof Double) || !(rightVal instanceof Double)) {
            throw new MiniException("Matematikai muvelet ('" + operator +
                    "') csak szamokon vegezheto el. Talalt tipusok: " +
                    leftVal.getClass().getSimpleName() + " es " +
                    rightVal.getClass().getSimpleName());
        }

        // Biztonságos kasztolás:
        // Mivel az IF ágon átjutott, Double-ről van szó
        double leftNum = (Double) leftVal;
        double rightNum = (Double) rightVal;

        // Művelet elvégzése
        switch (operator) {
            case '+': return leftNum + rightNum;
            case '-': return leftNum - rightNum;
            case '*': return leftNum * rightNum;
            case '/':
                // Futásidejű védelem a nullával való osztás ellen
                if (rightNum == 0) {
                    throw new MiniException("Nullaval osztas!");
                }
                return leftNum / rightNum;
            default:
                throw new MiniException("Ismeretlen operator: " + operator);
        }
    }
}