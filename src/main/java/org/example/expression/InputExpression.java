package org.example.expression;

import org.example.core.Environment;
import java.util.Scanner;

/*
 * Az InputExpression felelős a standard bemenet (konzol)
 * olvasásáért futásidőben.
 * Ez az osztály valósítja meg
 * az "Implicit Type Coercion" (automatikus típuskonverzió)
 * elvét a bemenetek kezelésénél.
 * A nyelv megpróbálja kitalálni (inferálni) a beírt adat típusát,
 * mentesítve a programozót a manuális
 * típuskonvertáló függvények használata alól.
 */
public class InputExpression implements Expression {

    @Override
    public Object evaluate(Environment env) {

        Scanner scanner = new Scanner(System.in);
        String inputStr = scanner.nextLine();

        // Automatikus típusfelismerés
        try {
            // 1. Megpróbálja a beírt szöveget
            // lebegőpontos számmá (Double) alakítani.
            return Double.parseDouble(inputStr);

        } catch (NumberFormatException e) {

            // 2. Ha a konverzió elbukik
            // akkor a rendszer elkapja a hibát,
            // és visszatér az eredeti, nyers szöveggel.
            return inputStr;
        }
    }
}