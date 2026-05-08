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

    private String name;

    private List<String> parameters;
    public List<Statement> body;


    public FunctionStatement(String name, List<String> parameters, List<Statement> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }


    @Override
    public void execute(Environment env) {

        // A függvényeket a szótárban (Map) tárolja
        env.setVariable(name, this);
    }

    // GETTEREK (A CallExpression számára)
    public List<String> getParameters() { return parameters; }
    public List<Statement> getBody() { return body; }
}