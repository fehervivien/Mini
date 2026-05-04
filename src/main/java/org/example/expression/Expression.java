package org.example.expression;

import org.example.core.Environment;

/*
 * Az Expression interfész egy közös szabálygyűjtemény,
 * ami garantálja, hogy a szintaxisfa minden eleme
 * (számok, szövegek, műveletek) ugyanúgy működjön:
 * mindegyiktől meg lehessen kérdezni az értékét
 * az evaluate() függvénnyel.
 */
public interface Expression {
    /*
     * Kiértékeli az adott kifejezést, és visszatér annak
     * futásidejű (dinamikus) értékével.

     * @param env: Az aktuális végrehajtási környezet (Environment),
     * amely tartalmazza a memóriában éppen elérhető lokális
     * és globális változókat.
     *
     * @return: A kifejezés eredménye. Mivel a Mini nyelv
     * dinamikusan típusos (nincs előre definiálva, hogy
     * egy változó szám vagy szöveg), a visszatérési érték
     * egy általános Object.
     */
    Object evaluate(Environment env);
}