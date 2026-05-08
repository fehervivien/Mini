package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import java.util.List;


/*
 * A ForStatement (For ciklus utasítás) a nyelv determinisztikus ismétlődő
 * vezérlési szerkezetét reprezentálja.
 * Egy for ciklus 4 jól elkülöníthető részből áll:
 * inicializálás,
 * feltételvizsgálat,
 * ciklusmag végrehajtása
 * és léptetés.
 */
public class ForStatement implements Statement {

    // 1. Inicializálás (pl. i = 0)
    private Statement init;

    // 2. Feltétel (pl. i < 10)
    private Expression condition;

    // 3. Léptetés (pl. i = i + 1)
    private Statement step;

    // 4. A ciklus törzse
    private List<Statement> body;


    public ForStatement(Statement init, Expression condition, Statement step, List<Statement> body) {
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
    }


    @Override
    public void execute(Environment env) {
        // 1. Inicializálás
        init.execute(env);

        while (true) {
            // 2. Feltétel ellenőrzése
            Object result = condition.evaluate(env);

            if (!(result instanceof Double)) {
                throw new RuntimeException("A for ciklus feltételének számnak kell lennie!");
            }

            if ((Double) result == 0.0) {
                break;
            }

            // 3. A ciklus törzse (magja)
            for (Statement stmt : body) {
                stmt.execute(env);
            }

            // 4. Léptetés
            step.execute(env);
        }
    }
}