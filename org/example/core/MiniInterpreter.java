package org.example.core;

import org.example.statement.Statement;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * A MiniInterpreter a program belépési pontja (Main osztály) és a nyelv irányítója.
 * Ez az osztály felelős azért, hogy összekösse a rendszer rétegeit:
 * beolvassa a forráskódot a fájlrendszerből,
 * átadja a Parsernek (szintaktikai elemző),
 * majd létrehozza a globális környezetet és elindítja a végrehajtást.
 */

public class MiniInterpreter {

    /* A végrehajtási folyamat (Interpreter Pipeline) központi logikája.
     * Ez a metódus alakítja át a nyers szöveget futó programmá.
     * @param sourceCode: A végrehajtandó Mini nyelvű forráskód nyers szövegként (String).
     */
    public void interpret(String sourceCode) {
        try {
            // 1. FÁZIS: Elemzés (Parsing)
            // Létrehoz egy elemzőt, ami a szöveges kódot egy Absztrakt Szintaxisfává (AST),
            // egy végrehajtható parancsokból (Statement) álló listává alakítja.
            Parser parser = new Parser();
            List<Statement> statements = parser.parse(sourceCode);

            // 2. FÁZIS: Környezet (Environment) inicializálása
            // Létrehozza a globális hatókört, amely a főprogram
            // változóit és függvényeit tárolja.
            Environment environment = new Environment();

            // 3. FÁZIS: Végrehajtás (Execution)
            // Végigmegy az elemző által generált parancsokon,
            // és sorban lefuttatja őket.
            for (Statement statement : statements) {
                statement.execute(environment);
            }
        } catch (Exception e) {
            // Hibakezelés: Ha a futás (vagy az elemzés) során
            // bármilyen hiba történik, itt elkapja és kiírja
            // a felhasználónak.
            System.err.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* A Java program szabványos belépési pontja.
     * Parancssori argumentumként várja a futtatandó
     * Mini forráskód fájl elérési útját.
     * @param args: Parancssori argumentumok
     * (a 0. indexű elemnek kell lennie a fájlnévnek).
     */
    public static void main(String[] args) {

        // 1. Validáció: Ellenőrzi, hogy a felhasználó
        // megadta-e a futtatandó fájlt
        if (args.length == 0) {
            System.err.println("Hiba: Nem adtál meg futtatandó .mini fájlt!");
            System.err.println("Használat: java org.example.core.MiniInterpreter <fajlnev.mini>");
            // Hibás indítás esetén kilép a programból
            return;
        }
        // Az első argumentum tartalmazza
        // a fájl nevét / elérési útját
        String filePath = args[0];
        String sourceCode;

        // 2. Fájlbeolvasás (I/O művelet):
        // Megpróbálja a megadott fájl teljes tartalmát
        // egyetlen String változóba beolvasni.
        try {
            sourceCode = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            // Ha a fájl nem létezik, vagy nincs hozzá
            // olvasási jogosultság, hibaüzenetet ad.
            System.err.println("Hiba a fájl olvasása közben (" + filePath + "): " + e.getMessage());
            return;
        }

        // 3. Rendszer indítása:
        // Ha a beolvasás sikeres volt, példányosítja
        // az interpretert és átadja neki a kódot.
        MiniInterpreter interpreter = new MiniInterpreter();

        System.out.println("--- MiniNyelv futtatása: " + filePath + " ---");
        interpreter.interpret(sourceCode);
        System.out.println("--- Futtatás vége ---");
    }
}