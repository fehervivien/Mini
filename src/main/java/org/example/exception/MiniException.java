package org.example.exception;

/*
 * A MiniException a nyelv saját, egyedi hibaosztálya
 * (Custom Exception).
 * Célja, hogy egyértelműen elkülönítse a Mini nyelv
 * szintaktikai és futási hibáit a mögöttes Java
 * Virtuális Gép (JVM) belső hibáitól.
 */


/* Konstruktor (példányosítja a nyelvspecifikus kivételt).
 * @param message: A felhasználónak szánt, olvasható hibaüzenet
 */
public class MiniException extends RuntimeException {
    public MiniException(String message) {
        super(message);
    }
}