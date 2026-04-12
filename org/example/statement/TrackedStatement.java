package org.example.statement;

import org.example.core.Environment;
import org.example.exception.MiniException;
import org.example.exception.ReturnException;

/* A TrackedStatement egy burkoló (Wrapper) osztály,
 * amely a Decorator (Díszítő) szoftvertervezési mintát
 * valósítja meg. Feladata, hogy transzparens módon kiegészítse
 * a normál utasításokat (Statement) egy metaadattal:
 * az eredeti forráskódbeli sorszámmal. Ezzel teszi
 * lehetővé a precíz, felhasználóbarát hibakeresést
 * (Debugging) anélkül, hogy az üzleti logikát
 * (pl. egy ciklus működését) bonyolítaná.
 */
public class TrackedStatement implements Statement {

    /* A becsomagolt, eredeti utasítás
      (pl. egy PrintStatement vagy IfStatement) */
    private Statement originalStatement;

    /* A sor száma, ahol ez az utasítás
       az eredeti .mini fájlban szerepelt */
    private int lineNumber;

    /* Konstruktor, amely becsomagolja az utasítást
       a metaadattal. */
    public TrackedStatement(Statement originalStatement, int lineNumber) {
        this.originalStatement = originalStatement;
        this.lineNumber = lineNumber;
    }

    /* A végrehajtás elindítása a hibakezelő
       "védőháló" alatt.
       @param env: Az aktuális futási környezet. */
    @Override
    public void execute(Environment env) {
        try {
            /* 1. LÉPÉS: Delegálás
               Megpróbálja futtatni az eredeti parancsot.
               A burkoló nem avatkozik be a működésbe,
               csak figyeli, hogy történik-e közben hiba. */
            originalStatement.execute(env);

        } catch (ReturnException e) {
            /* 2. LÉPÉS: Vezérlési kivételek továbbengedése (Bypass)
               A ReturnException NEM egy hiba, hanem a
               "Kivétel mint vezérlés" (Control Flow) eszköze.
               Ha a függvényből egy 'return' érték repül kifelé,
               azt érintetlenül tovább kell engednünk,
               különben a CallExpression sosem kapná meg
               az eredményt! */
            throw e;
        } catch (MiniException e) {
            /* 3. LÉPÉS: Valódi hibák elkapása és kontextualizálása
               Ha a futás során bárhol hiba történt az iderepül.
               A TrackedStatement elkapja a hibát, ráragasztja
               az eltárolt sorszámot, majd egy új, immár sorszámozott
               hibaként dobja tovább a főprogram felé. */
            throw new MiniException("Futasi hiba a(z) " + lineNumber + ". sorban -> " + e.getMessage());
        }
    }
}