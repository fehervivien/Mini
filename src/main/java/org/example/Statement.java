package org.example;

/**
 * Interface egy 'Parancshoz' (Statement).
 * A parancsok nem adnak vissza értéket, csak csinálnak valamit.
 * Pl.: var, print, ...
 */
public interface Statement {
    void execute(Environment env);
}
