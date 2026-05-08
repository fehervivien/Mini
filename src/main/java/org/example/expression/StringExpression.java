package org.example.expression;

import org.example.core.Environment;

/*
 * A StringExpression osztály a szövegeket
 * reprezentálja az Absztrakt Szintaxisfában (AST).
 * Ez egy "falevél" (Leaf Node) típusú csomópont.
 * Szerepe, hogy a Parser által felismert, idézőjelektől
 * megfosztott tiszta szöveges adatot futásidőig
 * biztonságosan tárolja.
 */
public class StringExpression implements Expression {

    private String value;

    public StringExpression(String value) {
        this.value = value;
    }


    @Override
    public Object evaluate(Environment env) {
        // Egy string literál futásidejű értéke önmaga
        return value;
    }
}