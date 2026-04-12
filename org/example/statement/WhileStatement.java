package org.example.statement;

import org.example.core.Environment;
import org.example.expression.Expression;
import org.example.exception.MiniException; // ÚJ: Saját hibaosztály beemelése
import java.util.List;

/*
 * A WhileStatement (Elöltesztelős ciklus) a nyelv
 * alapvető iterációs (ismétlődő) vezérlési szerkezetét
 * reprezentálja az Absztrakt Szintaxisfában (AST).
 * Ez az osztály mutatja be a dinamikus újraértékelés
 * (Dynamic Re-evaluation) koncepcióját: a ciklus
 * feltételét minden egyes iteráció előtt újra ki
 * kell számolni a legfrissebb memóriaállapot alapján.
 */
public class WhileStatement implements Statement {

    // A ciklus futásának feltétele (pl. x < 10)
    private Expression condition;

    // A ciklus törzse
    private List<Statement> body;

    /* Konstruktor, amely felépíti a ciklus
       AST csomópontját. */
    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    /* A ciklus futásidejű végrehajtása.
       @param env: Az aktuális futási környezet
       (memória).
     */
    @Override
    public void execute(Environment env) {

        /* A Mini nyelv ciklusát a gazdanyelv (Java)
           egy végtelen ciklusával szimulálja.
           Ezt hívják "Host language iteration mapping"-nek. */
        while (true) {

            /* 1. LÉPÉS: A feltétel újraértékelése
               Ezt az Expression.evaluate() hívást
               minden körben meg KELL tenni, hiszen
               a ciklusmag (body) az előző körben
               valószínűleg megváltoztatta a változók
               értékét */
            Object result = condition.evaluate(env);

            /* 2. LÉPÉS: Szigorú típusellenőrzés:
               A vezérlési feltételnek garantáltan
               számnak (logikai értéknek) kell lennie. */
            if (!(result instanceof Double)) {
                throw new MiniException(
                        "Tipushiba: A 'while' feltetelnek szamnak (logikai erteknek) kell lennie. " +
                                "Kapott tipus: " + result.getClass().getSimpleName());
            }

            double val = (Double) result;

            /* 3. LÉPÉS: Kilépési feltétel (Break condition)
               A 0.0 - hamis érték. Ha a feltétel hamis, megszakítja
               a Java végtelen ciklust, és ezzel véget ér a Mini
               nyelvű ciklus is. */
            if (val == 0.0) {
                break;
            }

            /* 4. LÉPÉS: A ciklusmag végrehajtása
               Ha a feltétel igaz volt, sorban lefuttatjuk
               a blokkban lévő utasításokat. */
            for (Statement stmt : body) {
                stmt.execute(env);
            }
        }
    }
}