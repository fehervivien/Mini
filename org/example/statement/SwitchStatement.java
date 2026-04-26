package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import java.util.Map;
import java.util.LinkedHashMap;


public class SwitchStatement implements Statement {
    private final Expression condition;
    private final Map<Expression, Statement> cases;
    private final Statement defaultBranch;

    public SwitchStatement(Expression condition, Map<Expression, Statement> cases, Statement defaultBranch) {
        this.condition = condition;
        this.cases = cases;
        this.defaultBranch = defaultBranch;
    }

    @Override
    public void execute(Environment env) {
        // 1. Kiértékeljük a fő kifejezést (pl. x értéke legyen 2)
        Object valueToMatch = condition.evaluate(env);
        boolean matched = false;

        // 2. Végigmegyünk az összes case ágon
        for (Map.Entry<Expression, Statement> entry : cases.entrySet()) {
            Object caseValue = entry.getKey().evaluate(env);

            // 3. Összehasonlítás (Java .equals-t használunk a dinamikus típusok miatt)
            if (valueToMatch.equals(caseValue)) {
                entry.getValue().execute(env);
                matched = true;
                break; // Implicit break: ha megvan a találat, nem megyünk tovább
            }
        }

        // 4. Ha egyik case sem talált, és van default ág, futtassuk azt
        if (!matched && defaultBranch != null) {
            defaultBranch.execute(env);
        }
    }
}
