package com.axibase.tsd.api.registry;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Dmitry Korchagin.
 */
public class PropertyRegistry {
    private static PropertyRegistry instance = null;
    private Set<String> entitySet = new TreeSet<>();
    private Set<String> typeSet = new TreeSet<>();

    public static PropertyRegistry getInstance() {
        if (null == instance) {
            instance = new PropertyRegistry();
        }
        return instance;
    }

    public void registerEntity(String value) {
        if (entitySet.contains(value)) {
            throw new IllegalArgumentException("Entity already registred");
        }
        entitySet.add(value);
    }

    public void registerType(String value) {
        if (typeSet.contains(value)) {
            throw new IllegalArgumentException("Type already registred");
        }
        typeSet.add(value);

    }
}
