package org.example;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A fő osztály, ami összefogja az interpretert.
 * Ez a verzió parancssori argumentumként vár egy futtatandó .mini fájlt.
 */
public class MiniInterpreter {

    // Végrehajtja a forráskódot, amit stringként kap.
    public void interpret(String sourceCode) {
        try {
            Parser parser = new Parser();
            List<Statement> statements = parser.parse(sourceCode);

            Environment environment = new Environment();
            for (Statement statement : statements) {
                statement.execute(environment);
            }
        } catch (Exception e) {
            System.err.println("Hiba történt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //A program belépési pontja.
    public static void main(String[] args) {

        // 1. Ellenőrizzük, hogy a felhasználó adott-e meg fájlnevet
        if (args.length == 0) {
            System.err.println("Hiba: Nem adtál meg futtatandó .mini fájlt!");
            System.err.println("Használat: java org.example.MiniInterpreter <fajlnev.mini>");
            return; // Kilépés
        }
        // Az első argumentum a fájl neve
        String filePath = args[0];
        String sourceCode;

        // 2. Próbáljuk meg beolvasni a fájl teljes tartalmát egy stringbe
        try {
            sourceCode = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Hiba a fájl olvasása közben (" + filePath + "): " + e.getMessage());
            return;
        }

        // 3. Ha a beolvasás sikeres, futtatjuk az interpretert
        MiniInterpreter interpreter = new MiniInterpreter();

        System.out.println("--- MiniNyelv futtatása: " + filePath + " ---");
        interpreter.interpret(sourceCode);
        System.out.println("--- Futtatás vége ---");
    }
}