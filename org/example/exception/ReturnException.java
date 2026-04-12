package org.example.exception;

/*
 * A ReturnException egy speciális osztály, amely technikailag egy kivétel (Exception),
 * de logikailag NEM hibát jelez.
 *
 * Ezt a szoftverfejlesztési mintát "Exception as Control Flow" (Kivétel mint vezérlés)
 * néven ismerjük az interpreterek fejlesztése során.
 *
 * Célja: Amikor a kód futása során egy 'return' utasításhoz érünk,
 * a Java kivételkezelési mechanizmusát használjuk arra, hogy azonnal
 * "kiugorjunk" a függvény tetszőleges mélységéből,
 * és magunkkal vigyük a visszatérési értéket a hívó félhez
 * (a CallExpression-höz).
 *
 *

Emiatt sorrend miatt kell ez az osztály hogy ne boruljon fel:
1. A CallExpression (a függvényhívás) elindítja a FunctionStatement-et.

2. A FunctionStatement elindítja a ForStatement-et (a ciklust).

3. A ForStatement minden körben elindítja az IfStatement-et.

4. Az IfStatement elindítja a ReturnStatement-et.
 */

public class ReturnException extends RuntimeException {

    /* A függvény által visszaadott érték, (a Mini nyelv dinamikusan típusos)
     * ami lehet szám (Double), szöveg (String), vagy bármi más (Object)).
     */
    private Object value;

    /*
     * Konstruktor, amely becsomagolja a visszatérési értéket a kivételbe.
     * @param value: A kifejezés kiértékelt eredménye
     */
    public ReturnException(Object value) {
        this.value = value;
    }

    /*
     * A becsomagolt érték lekérdezése.
     * Amikor a CallExpression elkapja ezt a kivételt a try-catch blokkjában,
     * ezzel a metódussal fogja "kicsomagolni" az eredményt, hogy a program
     * folytathassa a számolást.
     * @return: A függvényből visszatérő adat.
     */
    public Object getValue() {
        return value;
    }
}