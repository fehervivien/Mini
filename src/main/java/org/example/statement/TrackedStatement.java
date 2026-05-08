package org.example.statement;

import org.example.core.Environment;
import org.example.exception.MiniException;
import org.example.exception.ReturnException;

/*
 * A TrackedStatement feladata, hogy kiegészítse
 * a normál utasításokat (Statement) egy metaadattal
 * (az eredeti forráskódbeli sorszámmal).
 * Ezzel teszi lehetővé a precíz, felhasználóbarát
 * hibakeresést (Debugging).
 */
public class TrackedStatement implements Statement {

    // A becsomagolt, eredeti utasítás
    private Statement originalStatement;
    private int lineNumber;



    public TrackedStatement(Statement originalStatement, int lineNumber) {
        this.originalStatement = originalStatement;
        this.lineNumber = lineNumber;
    }


    @Override
    public void execute(Environment env) {
        try {
            // 1. Delegálás
            originalStatement.execute(env);

        } catch (ReturnException e) {
            // 2. Vezérlési kivételek továbbengedése (Bypass)
            throw e;
        } catch (MiniException e) {
            // 3. Valódi hibák elkapása és kontextualizálása
            throw new MiniException("Futasi hiba a(z) " + lineNumber + ". sorban -> " + e.getMessage());
        }
    }
}