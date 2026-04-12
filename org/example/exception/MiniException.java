package org.example.exception;

/*
 * A MiniException a nyelv saját, egyedi hibaosztálya (Custom Exception).
 * Célja, hogy egyértelműen elkülönítse a Mini nyelv szintaktikai és futási hibáit
 * (pl. "Ismeretlen változó", "Típusillesztési hiba") a mögöttes Java Virtuális Gép (JVM)
 * belső, rendszerint végzetes hibáitól.
 */


/* Konstruktor, amely példányosítja a nyelvspecifikus kivételt.
 * @param message: A felhasználónak szánt, olvasható hibaüzenet
 * (pl. "Hiba a 4. sorban...").
 */
public class MiniException extends RuntimeException {
    public MiniException(String message) {
        super(message);
    }
}