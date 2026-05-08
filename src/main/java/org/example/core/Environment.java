package org.example.core;

import org.example.exception.MiniException;

import java.util.HashMap;
import java.util.Map;

/*
* Az Environment (Környezet) osztály az interpreter egyik legfontosabb része,
* mert ez felel a hatókörök (Scope) kezeléséért.
* Ez teszi lehetővé, hogy a függvényeknek lehessenek saját, "privát" változóik,
* miközben továbbra is látják a főprogram változóit.
*/

public class Environment {

    private Map<String, Object> variables = new HashMap<>();

    private Environment parent;


    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void setVariable(String name, Object value) {

        if (parent != null && parent.containsVariable(name) && !variables.containsKey(name)) {
            parent.setVariable(name, value);
        } else {
            variables.put(name, value);
        }
    }

    public Object getVariable(String name) {

        if (variables.containsKey(name)) return variables.get(name);

        if (parent != null) return parent.getVariable(name);

        throw new MiniException("Ismeretlen valtozo vagy fuggveny: '" + name + "'");
    }


    private boolean containsVariable(String name) {
        if (variables.containsKey(name)) return true;

        if (parent != null) return parent.containsVariable(name);

        return false;
    }
}