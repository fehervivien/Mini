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

    /* Konstruktor: elmenti a visszatérési
       kifejezés AST csomópontját. */
    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }

    /* Az utasítás végrehajtása.
       Kiszámolja a visszatérési értéket,
       majd becsomagolja azt, és továbbadja
       az értelmezőnek (Interpreter).
       @param env: Az aktuális (lokális) környezet. */
    @Override
    public void execute(Environment env) {
        /* 1. LÉPÉS: Érték kiértékelése és Null-ellenőrzés
           Ha van megadva kifejezés a return után,
           kiszámolja az értékét. Ha nincs, akkor
           alapértelmezettként a hamis értéket (0.0)
           adja vissza, hogy elkerülje
           a NullPointerException-t. */
        Object val = (expr != null) ? expr.evaluate(env) : 0.0;

        /* 2. LÉPÉS: A Kivétel eldobása:
           A ReturnException eldobásával azonnal
           megszakítja az összes környező ciklust
           és elágazást. Nem a Java natív return
           kulcsszavát használja, mert ez csak
           ebből az execute() metódusból lépne ki,
           és az értelmező folytatná a következő
           parancs végrehajtását. */
        throw new ReturnException(val);
    }
}