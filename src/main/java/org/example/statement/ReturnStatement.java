package org.example.statement;

import org.example.core.Environment;
import org.example.exception.ReturnException;
import org.example.expression.Expression;

/*
 * A ReturnStatement (Visszatérési utasítás)
 * felelős a függvények futásának megszakításáért
 * és a kiszámolt eredmény visszajuttatásáért
 * a hívó félhez (CallExpression).
 */
public class ReturnStatement implements Statement {
    // A visszaadandó kifejezés
    private Expression expr;


    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }


    @Override
    public void execute(Environment env) {
        // 1. Érték kiértékelése és Null-ellenőrzés
        Object val = (expr != null) ? expr.evaluate(env) : 0.0;

        // 2. A Kivétel eldobása
        throw new ReturnException(val);
    }
}