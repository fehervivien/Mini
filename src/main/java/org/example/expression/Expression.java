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

    Object evaluate(Environment env);
}