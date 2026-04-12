package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import java.util.List;


/*
 * A ForStatement (For ciklus utasítás) a nyelv determinisztikus ismétlődő
 * vezérlési szerkezetét reprezentálja az Absztrakt Szintaxisfában (AST).
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

    //Konstruktor a for ciklus felépítéséhez.
    public ForStatement(Statement init, Expression condition, Statement step, List<Statement> body) {
        this.init = init;
        this.condition = condition;
        this.step = step;
        this.body = body;
    }

    /* A for ciklus futásidejű végrehajtása.
     * @param env: Az aktuális környezet (memória).
     */
    @Override
    public void execute(Environment env) {
        // 1. LÉPÉS: Inicializálás
        // Ez a parancs szigorúan csak egyszer fut le,
        // a legelső kör előtt.
        init.execute(env);

        // A Java saját while(true) ciklusát használja
        // a Mini nyelv ciklusának szimulálására
        while (true) {
            // 2. LÉPÉS: Feltétel ellenőrzése
            // Minden kör elején kiértékeljük a feltételt
            Object result = condition.evaluate(env);

            // Szigorú típusellenőrzés:
            // A feltételnek logikai értéknek
            // (itt számnak) kell lennie
            if (!(result instanceof Double)) {
                throw new RuntimeException("A for ciklus feltételének számnak kell lennie!");
            }

            // Ha a feltétel eredménye 0.0 - hamis,
            // azonnal megszakítja a ciklust és kilép.
            if ((Double) result == 0.0) {
                break;
            }

            // 3. LÉPÉS: A ciklus törzsének (magjának)
            // végrehajtása
            // Végigmegy a blokkban lévő összes parancson
            for (Statement stmt : body) {
                stmt.execute(env);
            }

            /* 4. LÉPÉS: Léptetés
               Miután a mag lefutott, végrehajtja
               a léptető parancsot (pl. i = i + 1),
               mielőtt visszaugrana a feltételvizsgálathoz
               (2. lépés). */
            step.execute(env);
        }
    }
}