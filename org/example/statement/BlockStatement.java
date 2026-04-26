package org.example.statement;

import org.example.core.Environment;
import java.util.List;


public class BlockStatement implements Statement {
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void execute(Environment env) {
        // A blokk végrehajtása: sorban lefuttatja a benne lévő utasításokat
        for (Statement statement : statements) {
            statement.execute(env);
        }
    }
}
