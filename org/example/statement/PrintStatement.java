package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;

/*
 * A PrintStatement (Kiíró utasítás) a nyelv
 * standard kimenetét (Standard Output) kezeli.
 * a PrintStatement nem ad vissza semmit a memóriába, hanem a konzol
 * állapotát változtatja meg.
 */
public class PrintStatement implements Statement {

    // A kiírandó kifejezés
    private Expression expression;

    /* Konstruktor, amely elmenti a kiírandó
       kifejezés AST csomópontját. */
    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    /* Az utasítás végrehajtása: kiértékeli a kifejezést,
       majd az eredményt formázva a képernyőre írja.
       @param env: Az aktuális futási környezet. */
    @Override
    public void execute(Environment env) {

        /* 1. LÉPÉS: A kifejezés dinamikus kiértékelése
           A polimorfizmusnak köszönhetően a PrintStatement-nek nem kell tudnia,
           hogy pontosan mit ír ki. Csak meghívja az evaluate() metódust,
           és kap egy általános Object-et. */
        Object value = expression.evaluate(env);

        // 2. LÉPÉS: Típus-specifikus formázás és Kiírás (I/O művelet)-
        if (value instanceof String) {
            /* A szövegeket idézőjelek nélkül,
               tisztán írja ki a felhasználónak */
            System.out.println(value);
        } else if (value instanceof Double) {
            /* A számokat a Java natív lebegőpontos
               formátumában írjuk ki (pl. 5.0) */
            System.out.println(value);
        } else {
            /* Biztonsági ág null vagy a jövőben
               bevezetésre kerülő, ismeretlen típusok
               (pl. tömbök, objektumok) esetére. */
            System.out.println(value != null ? value.toString() : "null");
        }
    }
}