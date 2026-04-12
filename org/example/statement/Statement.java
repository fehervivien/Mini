package org.example.statement;

import org.example.core.Environment;

/*
 * A Statement (Utasítás/Parancs) interfész
 * az Absztrakt Szintaxisfa (AST) végrehajtható
 * csomópontjainak közös ősét definiálja.
 * Ez az interfész éles architekturális határvonalat
 * húz a Kifejezések (Expressions) és az Utasítások (Statements) közé.
 * Míg a kifejezések értéket állítanak elő, a Statement-ek a program
 * állapotát (State) módosítják mellékhatások (Side effects) révén.
 */
public interface Statement {
    /* Végrehajtja az adott utasítást.
       A metódus visszatérési típusa 'void'.
       Egy utasítás soha nem ad vissza adatot a hívónak,
       feladata kizárólag a memóriaterület (Environment)
       vagy a külvilág (I/O) manipulálása.
       @param env: Az aktuális futási környezet, amelyen
       az utasítás elvégzi a memóriamódosításokat
       (pl. változó létrehozása). */
    void execute(Environment env);
}
