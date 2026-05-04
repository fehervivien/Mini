package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import java.util.List;


/*
 * Az IfStatement (Feltételes Elágazás) a program
 * vezérlési folyamatának (Control Flow) egyik legfontosabb
 * eszköze. Ez az osztály felelős a futásidejű döntéshozatalért.
 * Egy kiértékelendő feltételből (Expression) és
 * végrehajtandó kódblokkokból (Statement listák) áll.
 */
public class IfStatement implements Statement {

    // A feltétel, amit ki kell értékelni (pl. x > 5)
    private Expression condition;

    /* Az "igaz" (THEN) ág: azon parancsok listája,
       amik akkor futnak le, ha a feltétel igaz */
    private List<Statement> thenBlock;

    /* A "hamis" (ELSE) ág: opcionális parancslista. */
    private List<Statement> elseBlock;

    // Konstruktor az elágazás felépítéséhez.
    public IfStatement(Expression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    /* Az elágazás futásidejű végrehajtása.
     * @param env: Az aktuális futási környezet (memória).
     */
    @Override
    public void execute(Environment env) {
        // 1. LÉPÉS: Kiértékeli a feltételt
        Object conditionResult = condition.evaluate(env);

        // 2. LÉPÉS: Szigorú típusellenőrzés a vezérlésben
        if (!(conditionResult instanceof Double)) {
            throw new RuntimeException(
                    "Típushiba: Az 'if' feltételnek számnak kell lennie (1.0 = true, 0.0 = false). " +
                            "Kapott típus: " + conditionResult.getClass().getSimpleName());
        }

        // Biztonságos kasztolás
        double conditionValue = (Double) conditionResult;

        // 3. LÉPÉS: Ágaztatás (Branching)
        if (conditionValue != 0.0) {
            for (Statement statement : thenBlock) {
                statement.execute(env);
            }
        } else {
            if (elseBlock != null) {
                for (Statement statement : elseBlock) {
                    statement.execute(env);
                }
            }
        }
    }
}