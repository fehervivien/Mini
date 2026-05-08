package org.example.statement;

import org.example.core.Environment;

/*
 * A Statement (Utasítás/Parancs) interfész
 * a program állapotát (State) módosítják
 * mellékhatások (Side effects) révén.
 */
public interface Statement {
    void execute(Environment env);
}
