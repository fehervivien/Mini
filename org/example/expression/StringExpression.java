package org.example.expression;

import org.example.core.Environment;

/*
 * A StringExpression osztály a szöveges literálokat
 * reprezentálja az Absztrakt Szintaxisfában (AST).
 * Ez egy "falevél" (Leaf Node) típusú csomópont.
 * Szerepe, hogy a Parser által felismert, idézőjelektől
 * megfosztott tiszta szöveges adatot futásidőig
 * biztonságosan tárolja.
 */
public class StringExpression implements Expression {

    // A beolvasott szöveg értéke (pl. "hello world")
    private String value;

    /* Konstruktor, amely elmenti a lexikális elemző (Parser)
     * által átadott szöveget.
     * @param value: A fix szöveges érték.
     */
    public StringExpression(String value) {
        this.value = value;
    }

    /* A kifejezés kiértékelése. Mivel ez egy fix literál,
     * a kiértékelés egyszerűen az eltárolt szöveg
     * visszaadását jelenti.
     * @param env: Az aktuális környezet.
     * @return: A szöveg objektumként (String).
     */
    @Override
    public Object evaluate(Environment env) {
        // Egy string literál futásidejű értéke önmaga
        return value;
    }
}