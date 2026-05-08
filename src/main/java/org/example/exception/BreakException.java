package org.example.exception;

/*
* A Java RuntimeException osztályából származik,
* és a vezérlésmegszakítás belső reprezentációjára
* szolgál. Nem hibaállapotot jelez, hanem az interpreter
* ezen kivétel segítségével továbbítja a megszakítási szándékot
* a beágyazott utasításblokkokból a befoglaló vezérlési szerkezet
* (például a SwitchStatement) felé, amely a kivétel elkapásával
*  zárja le a saját futási ciklusát.*/

public class BreakException extends RuntimeException {
    public BreakException() {
        super();
    }
}