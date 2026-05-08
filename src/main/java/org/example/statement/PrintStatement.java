package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;

/*
 * A PrintStatement (Kiíró utasítás) a nyelv
 * standard kimenetét (Standard Output) kezeli.
 * a PrintStatement nem ad vissza semmit a memóriába,
 * hanem a konzol állapotát változtatja meg.
 */
public class PrintStatement implements Statement {

    // A kiírandó kifejezés
    private Expression expression;


    public PrintStatement(Expression expression) {
        this.expression = expression;
    }


    @Override
    public void execute(Environment env) {

        // 1. A kifejezés dinamikus kiértékelése
        Object value = expression.evaluate(env);

        // 2. Típus-specifikus formázás és Kiírás (I/O művelet)
        if (value instanceof String) {
            System.out.println(value);
        } else if (value instanceof Double) {
            System.out.println(value);
        } else {
            System.out.println(value != null ? value.toString() : "null");
        }
    }
}