package org.example.statement;

import org.example.core.Environment;
import org.example.exception.BreakException;
import org.example.expression.Expression;
import java.util.Map;
import java.util.LinkedHashMap;

/*
* A SwitchStatement osztály egy kényelmi funkciót
* valósít meg, amely kiváltja a hosszú, egymásba
* ágyazott if-else sorozatokat.
* Amint az interpreter megtalálja a megfelelő ágat
* és végrehajtja azt, automatikusan kilép a szerkezetből.
* */


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
        // 1. Kiértékeli a fő kifejezést (pl. x értéke legyen 2)
        Object valueToMatch = condition.evaluate(env);
        boolean matched = false;

        try {
            // 2. Végigmegy az összes case ágon
            for (Map.Entry<Expression, Statement> entry : cases.entrySet()) {
                Object caseValue = entry.getKey().evaluate(env);

                // 3. Összehasonlítás
                if (matched || valueToMatch.equals(caseValue)) {
                    matched = true;
                    entry.getValue().execute(env);
                }
            }

            // 4. Ha egyik case sem talált, és van default ág, futtassa azt
            if (!matched && defaultBranch != null) {
                defaultBranch.execute(env);
            }
        } catch (BreakException e) {
            // A kiadott 'break' utasítás ide irányítja a vezérlést.
        }
    }
}
