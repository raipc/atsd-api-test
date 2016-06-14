package com.axibase.tsd.api.registry;

import java.util.Set;
import java.util.TreeSet;

public class SeriesRegistry {

    private static SeriesRegistry instance = null;

    private Set<String> entitySet = new TreeSet<>();
    private Set<String> metricSet = new TreeSet<>();

    public static SeriesRegistry getInstance() {
        if (null == instance) {
            instance = new SeriesRegistry();
        }
        return instance;
    }

    public void registerEntity(String value) {
        if (entitySet.contains(value)) {
            throw new IllegalArgumentException("Entity already registered: " + value);
        }
        entitySet.add(value);
    }

    public void registerMetric(String value) {
        if (metricSet.contains(value)) {
            throw new IllegalArgumentException("Metric already registered: " + value);
        }
        metricSet.add(value);
    }
}
