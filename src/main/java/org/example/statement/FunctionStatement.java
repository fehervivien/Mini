package org.example.statement;

import org.example.core.Environment;
import java.util.List;

/*
 * A FunctionStatement (Függvénydeklaráció utasítás)
 * felelős a felhasználó által definiált saját függvények
 * felépítéséért.
 * Ez az osztály NEM hajtja végre a függvény belsejében lévő kódot.
 * Kizárólag arra szolgál, hogy a függvény nevét, paramétereit és törzsét
 * elmentse a memóriába egy későbbi hívás (CallExpression) számára.
 */
public class FunctionStatement implements Statement {

    // A függvény egyedi neve
    private String name;

    // A paraméterek neveinek listája
    private List<String> parameters;

    /* A függvény törzse: a benne lévő
       végrehajtandó utasítások listája */
    public List<Statement> body;

    /* Konstruktor, amely elmenti a Parser
      által értelmezett függvény adatokat. */
    public FunctionStatement(String name, List<String> parameters, List<Statement> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    /* A deklaráció "végrehajtása".
       Magát a függvény objektumot (this)
       letárolja az aktuális környezet
       (Environment) változói között.
       @param env: Az aktuális környezet (memória). */
    @Override
    public void execute(Environment env) {

        // A függvényeket a szótárban (Map) tárolja
        env.setVariable(name, this);
    }

    // GETTEREK (A CallExpression számára)

    /* Visszaadja a paraméterek listáját,
       hogy a hívó fél tudja, hány
       argumentumot kell átadnia */
    public List<String> getParameters() { return parameters; }

    /* Visszaadja a függvény törzsét, hogy a hívó fél
       le tudja futtatni azt egy lokális környezetben */
    public List<Statement> getBody() { return body; }
}