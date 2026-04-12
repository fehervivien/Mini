package org.example.expression;

import org.example.core.Environment;

/*
 * A NumberExpression osztály a numerikus literálokat (fix számokat)
 * reprezentálja az Absztrakt Szintaxisfában (AST).
 * Ez egy "falevél" (Leaf Node) típusú csomópont:
 * nincsenek gyermek-kifejezései, és nem végez műveletet,
 * csupán egyetlen, a lexikális elemzés (Parsing) során
 * felismert értéket tárol.
 */
public class NumberExpression implements Expression {
    // A beolvasott szám értéke
    private double value;

    /* Konstruktor, amely elmenti a Parser által
     * felismert számot.
     * @param value: A fix számérték (pl. 10.5).
     */
    public NumberExpression(double value) { this.value = value; }

    /* A kifejezés "kiértékelése". Mivel ez egy literál, a kiértékelés
     * mindössze annyit tesz, hogy visszaadja az eltárolt értéket.
     * @param env: Az aktuális környezet.
     * @return: A szám objektumként (Double).
     * A háttérben a Java "Auto-boxing" (automatikus dobozolás)
     * mechanizmusa alakítja a primitív double-t Double objektummá.
     */
    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}