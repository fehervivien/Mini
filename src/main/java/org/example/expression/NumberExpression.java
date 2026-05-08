package org.example.expression;

import org.example.core.Environment;

/*
 * A NumberExpression osztály a számokat
 * reprezentálja az Absztrakt Szintaxisfában (AST).
 * Ez egy "falevél" (Leaf Node) típusú csomópont:
 * nincsenek gyermek-kifejezései, és nem végez műveletet,
 * csupán egyetlen, a lexikális elemzés (Parsing) során
 * felismert értéket tárol.
 */
public class NumberExpression implements Expression {
    private double value;

    public NumberExpression(double value) { this.value = value; }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}