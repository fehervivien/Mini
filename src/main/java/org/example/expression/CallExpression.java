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

    // A meghívott függvény neve (pl. "osszead")
    private String functionName;

    // A zárójelek között átadott értékek (argumentumok)
    // listája (pl. 5 és 10)
    private List<Expression> arguments;

    // Konstruktor a függvényhívás AST csomópontjának felépítéséhez.
    public CallExpression(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    /*
     * A függvényhívás végrehajtása és a visszatérési érték kiszámítása.
     * @param env: Az aktuális környezet (ahonnan a függvényt meghívták).
     * @return: A függvény által visszaadott érték (return).
     */
    @Override
    public Object evaluate(Environment env) {

        /* 1. BEÉPÍTETT FÜGGVÉNYEK (Standard Library):
           Ezek a nyelv hardkódolt, gyári alapfüggvényei.
           Nincs szükségük külön környezetre. */
        if (functionName.equals("random")) return Math.random();

        // Ha az argumentumlista nem üres,
        // kiértékeli az első paramétert
        // (pl. a round() számára)
        Object firstArg = arguments.isEmpty() ? null : arguments.get(0).evaluate(env);
        if (functionName.equals("round")) return (double) Math.round((Double) firstArg);
        if (functionName.equals("length")) return (double) ((String) firstArg).length();

        /* 2. SAJÁT, FELHASZNÁLÓI FÜGGVÉNYEK VÉGREHAJTÁSA:
           Megkeresi a függvényt a memóriában a neve alapján */
        Object funcObj = env.getVariable(functionName);

        // Futásidejű típusellenőrzés, függvény-e:
        if (!(funcObj instanceof FunctionStatement)) {
            throw new MiniException("'" + functionName + "' nem egy fuggveny!");
        }

        FunctionStatement func = (FunctionStatement) funcObj;
        // Arity-ellenőrzés (Paraméterszám vizsgálat)
        if (func.getParameters().size() != arguments.size()) {
            throw new MiniException("Hiba: a '" + functionName + "' fuggveny " + func.getParameters().size() + " argumentumot var!");
        }

        /* 3. HATÓKÖR (SCOPE) LÉTREHOZÁSA ÉS PARAMÉTERÁTADÁS:
           Létrehoz egy teljesen új, "Lokális" memóriát a függvénynek.
           Szülőként megkapja az aktuális környezetet
           (hogy lássa a globális változókat). */
        Environment localEnv = new Environment(env);

        // Call-by-Value paraméterátadás:
        // Kiértékeli az átadott kifejezéseket,
        // és beállítja őket a lokális környezetbe
        // a függvény által várt neveken (pl. 'a' és 'b').
        for (int i = 0; i < arguments.size(); i++) {
            localEnv.setVariable(func.getParameters().get(i), arguments.get(i).evaluate(env));
        }

        // 4. A FÜGGVÉNYTÖRZS VÉGREHAJTÁSA
        try {
            // Sorban végrehajtja a függvény belsejében
            // lévő parancsokat az új környezetben
            for (Statement stmt : func.getBody()) {
                stmt.execute(localEnv);
            }
        } catch (ReturnException e) {
            // Itt kapja el az "Exception as Control Flow"-t
            // Ha a függvény futása közben bárhol kiadták
            // a 'return' parancsot, megszakad a futás,
            // a kivétel iderepül, és ez visszaadja a benne lévő értéket.
            return e.getValue();
        }

        // Ha a függvény rendben lefutott,
        // de a felhasználó nem írt 'return' utasítást a kódba,
        // alapértelmezett értékként
        // 0.0-t ad vissza.
        return 0.0;
    }
}