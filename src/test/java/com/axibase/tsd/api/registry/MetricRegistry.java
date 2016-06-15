package com.axibase.tsd.api.registry;

import java.util.Set;
import java.util.TreeSet;

public class MetricRegistry {

    private static MetricRegistry instance = null;

    private Set<String> metricSet = new TreeSet<>();

    public static MetricRegistry getInstance() {
        if (null == instance) {
            instance = new MetricRegistry();
        }
        return instance;
    }

    public void registerMetric(String value) {
        if (metricSet.contains(value)) {
            throw new IllegalArgumentException("Metric already registered: " + value);
        }
        metricSet.add(value);
    }
}
