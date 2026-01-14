package org.example;

import java.util.List;

public class WhileStatement implements Statement {

    private Expression condition;
    private List<Statement> body;

    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute(Environment env) {
        while (true) {
            // 1. Minden kör elején kiértékeli a feltételt
            Object result = condition.evaluate(env);

            // 2. Típusellenőrzés (biztos számot kap-e?)
            if (!(result instanceof Double)) {
                throw new RuntimeException("A while feltételnek számnak kell lennie!");
            }

            double val = (Double) result;

            // 3. Ha 0.0 (hamis), akkor kilép a ciklusból
            if (val == 0.0) {
                break;
            }

            // 4. Ha igaz, lefuttatjuk a ciklus magját
            for (Statement stmt : body) {
                stmt.execute(env);
            }

        }
    }
}