package org.example;

import java.util.List;

/**
 * Egy IF-THEN-ELSE parancsot reprezentál.
 * Tárolja a feltételt, a 'then' blokk parancsait és az 'else' blokk parancsait.
 */
public class IfStatement implements Statement {

    private Expression condition;
    private List<Statement> thenBlock;
    private List<Statement> elseBlock; // Lehet null, ha nincs else ág

    public IfStatement(Expression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public void execute(Environment env) {
        // 1. Kiértékeljük a feltételt, Object-ként kapjuk meg
        Object conditionResult = condition.evaluate(env);

        // 2. Ellenőrizzük, hogy a feltétel Eredménye szám-e
        // Az 'if' feltételeknek (pl. x > 5) továbbra is 1.0 (true) vagy 0.0 (false)
        // értéket kell visszaadniuk.
        if (!(conditionResult instanceof Double)) {
            throw new RuntimeException(
                    "Típushiba: Az 'if' feltételnek számnak kell lennie (1.0 = true, 0.0 = false). " +
                            "Kapott típus: " + conditionResult.getClass().getSimpleName());
        }

        // 3. Kasztolás Double-re
        double conditionValue = (Double) conditionResult;

        if (conditionValue != 0.0) {
            // "True" ág: végrehajtjuk a 'then' blokkot
            for (Statement statement : thenBlock) {
                statement.execute(env);
            }
        } else {
            // "False" ág: végrehajtjuk az 'else' blokkot, ha létezik
            if (elseBlock != null) {
                for (Statement statement : elseBlock) {
                    statement.execute(env);
                }
            }
        }
    }
}