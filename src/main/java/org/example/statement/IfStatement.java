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

    // A feltétel, amit ki kell értékelni
    private Expression condition;

    private List<Statement> thenBlock;
    private List<Statement> elseBlock;

    public IfStatement(Expression condition, List<Statement> thenBlock, List<Statement> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }


    @Override
    public void execute(Environment env) {
        // 1. Kiértékeli a feltételt
        Object conditionResult = condition.evaluate(env);

        // 2. Típusellenőrzés a vezérlésben
        if (!(conditionResult instanceof Double)) {
            throw new RuntimeException(
                    "Típushiba: Az 'if' feltételnek számnak kell lennie (1.0 = true, 0.0 = false). " +
                            "Kapott típus: " + conditionResult.getClass().getSimpleName());
        }

        // Biztonságos kasztolás
        double conditionValue = (Double) conditionResult;

        // 3. Ágaztatás (Branching)
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