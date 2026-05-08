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


    public ReturnException(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}