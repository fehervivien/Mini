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

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Hiba: Nem adtál meg futtatandó .mini fájlt!");
            System.err.println("Használat: java org.example.core.MiniInterpreter <fajlnev.mini>");
            return;
        }

        String filePath = args[0];
        String sourceCode;

        try {
            sourceCode = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Hiba a fájl olvasása közben (" + filePath + "): " + e.getMessage());
            return;
        }

        MiniInterpreter interpreter = new MiniInterpreter();

        System.out.println("--- MiniNyelv futtatása: " + filePath + " ---");
        interpreter.interpret(sourceCode);
        System.out.println("--- Futtatás vége ---");
    }
}