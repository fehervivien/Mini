package org.example;

/**
 * Egy sima számot reprezentáló kifejezés (pl. 10, 3.14).
 * MÓDOSÍTÁS: Object-et ad vissza Double helyett.
 */
public class NumberExpression implements Expression {
    private double value;
    public NumberExpression(double value) { this.value = value; }

    @Override
    public Object evaluate(Environment env) {
        // Egy Double objektumot adunk vissza
        return value;
    }
}