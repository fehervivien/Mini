package org.example.expression;

import org.example.core.Environment;

/*
 * A VariableExpression (Változó Kifejezés) osztály
 * azokat a csomópontokat reprezentálja az AST-ben,
 * ahol egy változó értékét ki kell olvasni (pl. 'x' vagy 'terulet').
 * Ez az osztály felel a Változófeloldásért (Variable Resolution).
 * Ez a híd a statikus forráskód (a változó puszta neve) és a dinamikus
 * futásidejű memória (a változóhoz rendelt tényleges adat) között.
 */
public class VariableExpression implements Expression {

    private String name;

    public VariableExpression(String name) { this.name = name; }

    @Override
    public Object evaluate(Environment env) {
        return env.getVariable(name);
    }
}