package org.example.exception;

/*
 * Ez egy speciális osztály, amely technikailag
 * egy kivétel (Exception), de logikailag nem
 * hibát jelez. Amikor a kód futása során egy
 * return utasításhoz ér, a Java kivételkezelési
 * mechanizmusát használja arra, hogy azonnal
 * kilépjen a függvény adott részéből, és megtartsa
 * a visszatérési értéket a hívó félnek (CallExpression).
 */

public class ReturnException extends RuntimeException {

    // A függvény által visszaadott érték
    private Object value;

    /*
     * Konstruktor, amely beleteszi a visszatérési
     * értéket a kivételbe.
     * @param value: A kifejezés kiértékelt eredménye
     */
    public ReturnException(Object value) {
        this.value = value;
    }

    /*
     * A beletett érték lekérdezése.
     * Amikor a CallExpression elkapja ezt a
     * kivételt a try-catch blokkjában,
     * ezzel a metódussal fogja megkapni
     * az eredményt, hogy a program
     * folytathassa a számolást.
     * @return: A függvényből visszatérő adat.
     */
    public Object getValue() {
        return value;
    }
}