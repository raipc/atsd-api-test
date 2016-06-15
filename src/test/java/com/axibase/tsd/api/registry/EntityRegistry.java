package com.axibase.tsd.api.registry;

import java.util.Set;
import java.util.TreeSet;


public class EntityRegistry {

    private static EntityRegistry instance = null;

    private Set<String> entitySet = new TreeSet<>();

    public static EntityRegistry getInstance() {
        if (null == instance) {
            instance = new EntityRegistry();
        }
        return instance;
    }

    public void registerEntity(String value) {
        if (entitySet.contains(value)) {
            throw new IllegalArgumentException("Entity already registered: " + value);
        }
        entitySet.add(value);
    }
}
