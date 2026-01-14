package org.example;

/**
 * Változó definiálását vagy értékadását reprezentáló parancs.
 * MÓDOSÍTÁS: Object-et kezel Double helyett.
 */
public class VarAssignStatement implements Statement {
    private String varName;
    private Expression expression;

    public VarAssignStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public void execute(Environment env) {
        // Kiértékeljük a kifejezést (jobb oldalt)
        Object value = expression.evaluate(env);
        // Beállítjuk a változót a környezetben
        env.setVariable(varName, value);
    }
}