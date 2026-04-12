package org.example.expression;

import org.example.core.Environment;

/*
 * Az Expression (Kifejezés) interfész az Absztrakt Szintaxisfa (AST)
 * adat-kiértékelő csomópontjainak közös ősét és "szerződését"
 * (contract) definiálja.

 * Az Interpreter tervezési minta egyik alappillére:
 * minden kifejezés (egyszerű szám, matematikai művelet,
 * összetett függvényhívás) képes önmagát "kiértékelni"
 * egy adott környezetben.
 */
public interface Expression {
    /*
     * Kiértékeli az adott kifejezést, és visszatér annak
     * futásidejű (dinamikus) értékével.

     * @param env: Az aktuális végrehajtási környezet (Environment),
     * amely tartalmazza a memóriában éppen elérhető lokális
     * és globális változókat.
     * @return: A kifejezés eredménye. Mivel a Mini nyelv
     * dinamikusan típusos (nincs előre definiálva, hogy
     * egy változó szám vagy szöveg), a visszatérési érték
     * egy általános Object.

     * Ez a gyakorlatban leggyakrabban Double (szám)
     * vagy String (szöveg) típusúként fog realizálódni.
     */
    Object evaluate(Environment env);
}