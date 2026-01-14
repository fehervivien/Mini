package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * A 'Környezet' (vagy 'Context') tárolja a változók neveit és értékeit.
 * A térkép (Map) most már Object-eket tárol, nem csak Double-t.
 */
public class Environment {
    private Map<String, Object> variables = new HashMap<>();

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object getVariable(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Ismeretlen változó: '" + name + "'");
        }
        return variables.get(name);
    }
}