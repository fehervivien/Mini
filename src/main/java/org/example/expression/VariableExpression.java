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
    // A változó azonosítója (neve),
    // amit a Parser olvasott be a kódból
    private String name;

    /* Konstruktor, amely eltárolja
     * a kiolvasandó változó nevét.
     * @param name: A változó neve
     */
    public VariableExpression(String name) { this.name = name; }

    /* A kifejezés kiértékelése.
     * Mivel a változó értéke a program futása során
     * bármikor megváltozhat, ezt az értéket "frissen"
     * kell lekérdezni a memóriából.
     * @param env: Az aktuális hatókör (Környezet),
     * amelyben a program éppen fut
     * @return: A változó aktuális értéke
     */
    @Override
    public Object evaluate(Environment env) {
        /* A lokális és globális hatókörök végigkeresését
           delegálja az Environment osztály getVariable
           metódusának.*/
        return env.getVariable(name);
    }
}