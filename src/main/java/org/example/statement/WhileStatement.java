package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import org.example.exception.MiniException; // ÚJ: Saját hibaosztály beemelése
import java.util.List;

/*
 * A WhileStatement osztály, más néven az Elöltesztelős ciklus
 * a nyelv alapvető ismétlődő vezérlési szerkezetét
 * reprezentálja. Bemutatja a dinamikus újraértékelés
 * (Dynamic Re-evaluation) koncepcióját, vagyis a
 * ciklus feltételét minden egyes iteráció előtt újra
 * ki kell számolni a legfrissebb memóriaállapot alapján.
 */
public class WhileStatement implements Statement {

    // A ciklus futásának feltétele
    private Expression condition;

    // A ciklus törzse
    private List<Statement> body;


    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }


    @Override
    public void execute(Environment env) {
        while (true) {
            // 1. A feltétel újraértékelése
            Object result = condition.evaluate(env);

            // 2. Típusellenőrzés
            if (!(result instanceof Double)) {
                throw new MiniException(
                        "Tipushiba: A 'while' feltetelnek szamnak (logikai erteknek) kell lennie. " +
                                "Kapott tipus: " + result.getClass().getSimpleName());
            }

            double val = (Double) result;

            // 3. Kilépési feltétel (0.0 = hamis)
            if (val == 0.0) {
                break;
            }

            // 4. A ciklusmag végrehajtása hibakezeléssel
            try {
                for (Statement stmt : body) {
                    stmt.execute(env);
                }
            } catch (org.example.exception.BreakException e) {
                break;
            }
        }
    }
}