package org.example.statement;

import org.example.core.Environment;
import org.example.exception.ReturnException;
import org.example.expression.Expression;

/*
 * A ReturnStatement (Visszatérési utasítás)
 * felelős a függvények futásának megszakításáért
 * és a kiszámolt eredmény visszajuttatásáért
 * a hívó félhez (CallExpression).
 * Az osztály indítja el az "Exception as Control Flow"
 * (Kivétel mint vezérlési szerkezet) mechanizmust azáltal,
 * hogy a normál vezérlésátadás helyett szándékosan
 * egy futásidejű kivételt (Exception) dob.
 */
public class ReturnStatement implements Statement {
    // A visszaadandó kifejezés
    private Expression expr;

    /* Konstruktor, amely elmenti a visszatérési
       kifejezés AST csomópontját. */
    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }

    /* Az utasítás végrehajtása.
       Kiszámolja a visszatérési értéket,
       majd becsomagolja azt, és "kilövi"
       az értelmező (Interpreter) hívási láncán (Call Stack) felfelé.
       @param env: Az aktuális (lokális) környezet. */
    @Override
    public void execute(Environment env) {
        /* 1. LÉPÉS: Érték kiértékelése és Null-ellenőrzés
           Ha van megadva kifejezés a return után,
           kiszámolja az értékét. Ha nincs (üres return),
           akkor alapértelmezettként a Mini nyelv "void"
           megfelelőjét, a 0.0-t adja vissza, hogy elkerülje
           a NullPointerException-t. */
        Object val = (expr != null) ? expr.evaluate(env) : 0.0;

        /* 2. LÉPÉS: A "Füstbomba" (Kivétel) eldobása
           Kifejezetten NEM a Java natív 'return'
           kulcsszavát használjuk. A natív return csak ebből
           az execute() metódusból lépne ki, és az értelmező
           folytatná a következő parancs végrehajtását.
           A ReturnException eldobásával azonnal megszakítja
           az összes környező ciklust és elágazást. */
        throw new ReturnException(val);
    }
}