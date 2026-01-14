package org.example;

/**
 * Egy változó nevét reprezentáló kifejezés (pl. x, terulet).
 * MÓDOSÍTÁS: Object-et ad vissza Double helyett.
 */
public class VariableExpression implements Expression {
    private String name;
    public VariableExpression(String name) { this.name = name; }

    @Override
    public Object evaluate(Environment env) {
        // Egy változó értéke az, amit a környezetben tárolunk
        return env.getVariable(name);
    }
}