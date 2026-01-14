package org.example;

import java.util.List;

public class ForStatement implements Statement {

    private Statement init;
    private Expression condition;
    private Statement step;
    private List<Statement> body;

    public ForStatement(Statement init, Expression condition, Statement step, List<Statement> body) {
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        // 1. Inicializálás (ez csak egyszer fut le az elején)
        init.execute(env);

        while (true) {
            // 2. Feltétel ellenőrzése
            Object result = condition.evaluate(env);
            if (!(result instanceof Double)) {
                throw new RuntimeException("A for ciklus feltételének számnak kell lennie!");
            }

            // Ha 0.0 (hamis), kilépünk
            if ((Double) result == 0.0) {
                break;
            }

            // 3. A ciklus törzsének végrehajtása
            for (Statement stmt : body) {
                stmt.execute(env);
            }

            // 4. Léptetés (a kör végén)
            step.execute(env);
        }
    }
}