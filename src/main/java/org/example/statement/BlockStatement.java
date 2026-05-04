package org.example.statement;

import org.example.core.Environment;
import java.util.List;

/*
* A BlockStatement osztály az utasítások csoportosítására
* szolgál.
* Lehetővé teszi több különálló Statement összekapcsolását
* egyetlen logikai egységbe, ami elengedhetetlen az összetett
* vezérlési szerkezetek, mint például az ágazatok (if) és
* ciklusok (while) törzsének megvalósításához.
* A végrehajtás során a blokk a megadott
* sorrendben futtatja le a benne tárolt utasításlistát.
* Lényegében ez az osztály összetartja a kapcsos zárójelek
* közötti kódrészleteket.
*/

public class BlockStatement implements Statement {
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void execute(Environment env) {
        // A blokk végrehajtása: sorban lefuttatja a benne lévő utasításokat
        for (Statement statement : statements) {
            statement.execute(env);
        }
    }
}
