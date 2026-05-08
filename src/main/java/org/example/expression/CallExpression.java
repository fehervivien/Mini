package org.example.expression;

import org.example.statement.FunctionStatement;
import org.example.statement.Statement;
import org.example.core.Environment;
import org.example.exception.MiniException;
import org.example.exception.ReturnException;
import java.util.List;

/**
 * A CallExpression (Függvényhívás) osztály felelős egy már definiált függvény
 * futásidejű végrehajtásáért és az eredményének lekéréséért.
 * Kezeli a beépített standard függvényeket (pl. random, round),
 * a felhasználó által létrehozott (func) függvényeket.
 * Ez az osztály végzi a paraméterek átadását és a
 * lokális hatókör (Scope) felépítését is.
 */

public class CallExpression implements Expression {

    private String functionName;
    private List<Expression> arguments;


    public CallExpression(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }


    @Override
    public Object evaluate(Environment env) {

        // 1. Beépített függvények
        if (functionName.equals("random")) return Math.random();


        Object firstArg = arguments.isEmpty() ? null : arguments.get(0).evaluate(env);
        if (functionName.equals("round")) return (double) Math.round((Double) firstArg);
        if (functionName.equals("length")) return (double) ((String) firstArg).length();

        // 2. Saját, felhasználói függvények végrehajtása
        Object funcObj = env.getVariable(functionName);

        // Futásidejű típusellenőrzés
        if (!(funcObj instanceof FunctionStatement)) {
            throw new MiniException("'" + functionName + "' nem egy fuggveny!");
        }

        FunctionStatement func = (FunctionStatement) funcObj;
        // Paraméterszám vizsgálat
        if (func.getParameters().size() != arguments.size()) {
            throw new MiniException("Hiba: a '" + functionName + "' fuggveny " + func.getParameters().size() + " argumentumot var!");
        }

        // 3. Hatókör (scope) létrehozása és paraméter átadás
        Environment localEnv = new Environment(env);

        // Call-by-Value paraméterátadás
        for (int i = 0; i < arguments.size(); i++) {
            localEnv.setVariable(func.getParameters().get(i), arguments.get(i).evaluate(env));
        }

        // 4. A függvénytörzs végrehajtása
        try {
            for (Statement stmt : func.getBody()) {
                stmt.execute(localEnv);
            }
        } catch (ReturnException e) {
            return e.getValue();
        }

        // Hamis érték
        return 0.0;
    }
}