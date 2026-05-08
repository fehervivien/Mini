package org.example.statement;

import org.example.core.Environment;
import org.example.exception.BreakException;

/*
* A BreakStatement osztály egy vezérlésátadó utasítás
* vagyis a vezérlés megszakításáért felelős.
* Az execute metódusa nem végez számításokat,
* hanem egy egyedi BreakException kivételt dob.
* Ez a megoldás lehetővé teszi, hogy a Java
* kivételkezelési mechanizmusát használva ki
* kényszerítse a kilépést az aktuális végrehajtási
* blokkból,amelyet a magasabb szintű vezérlési
* szerkezetek (mint a SwitchStatement) fognak kezelni.*/

public class BreakStatement implements Statement {
    @Override
    public void execute(Environment env) {
        throw new BreakException();
    }
}