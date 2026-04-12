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

    /*
     * Végrehajtja a beolvasást a konzolról,
     * és visszaadja a beolvasott adatot a nyelv
     * számára megfelelő (Double vagy String) formátumban.
     * @param env: Az aktuális környezet (itt nem használjuk,
     * mert a beolvasás globális).
     * @return: A beírt adat
     */
    @Override
    public Object evaluate(Environment env) {
        // Létrehozunk egy Scannert a standard
        // bemenet (billentyűzet) figyelésére
        Scanner scanner = new Scanner(System.in);

        // Beolvas egy teljes sort,
        // amíg a felhasználó Entert nem nyom
        String inputStr = scanner.nextLine();

        // AUTOMATIKUS TÍPUSFELISMERÉS (Type Inference)
        try {
            // 1. Próba: Megpróbálja a beírt szöveget
            // lebegőpontos számmá (Double) alakítani.
            // Ha a felhasználó azt írta be, hogy "5" vagy "3.14", ez sikeres lesz.
            return Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            // 2. Próba: Ha a konverzió elbukik
            // (NumberFormatException-t dob a Java,
            // mert nem számot írtak be),
            // akkor a rendszer csendben elkapja a hibát,
            // és visszatér az eredeti, nyers szöveggel (String).
            return inputStr;
        }
    }
}