package com.axibase.tsd.api.registry;

import java.util.Set;
import java.util.TreeSet;

public class MessageRegistry {
    private Set<String> entitySet = new TreeSet<>();
    private Set<String> typeSet = new TreeSet<>();

    private static MessageRegistry instance = null;

    public static MessageRegistry getInstance() {
        if (null == instance) {
            instance = new MessageRegistry();
        }
        return instance;
    }

    public void registerEntity(String value) {
        if (entitySet.contains(value)) {
            throw new IllegalArgumentException("Entity already registered");
        }
        entitySet.add(value);
    }

    public void registerType(String value) {
        if (typeSet.contains(value)) {
            throw new IllegalArgumentException("Type already registered");
        }
        typeSet.add(value);
    }
}
