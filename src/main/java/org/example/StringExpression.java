package org.example;

/**
 * Egy szöveges literált (String) reprezentáló kifejezés.
 * Pl.: "hello world"
 */
public class StringExpression implements Expression {

    private String value;

    public StringExpression(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        // Egy string literál értéke önmaga
        return value;
    }
}