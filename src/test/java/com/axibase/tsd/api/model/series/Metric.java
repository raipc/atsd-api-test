package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.model.Model;
import com.axibase.tsd.api.registry.MetricRegistry;

import java.util.HashMap;
import java.util.Map;

public class Metric extends Model {
    private String name;
    private String enabled;
    private String persistent;
    private DataType dataType;
    private String timePrecision;
    private String retentionInterval;
    private Map<String, String> fields;

    public Metric(String name) {
        if (name != null) {
            MetricRegistry.getInstance().registerMetric(name);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getPersistent() {
        return persistent;
    }

    public void setPersistent(String persistent) {
        this.persistent = persistent;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getTimePrecision() {
        return timePrecision;
    }

    public void setTimePrecision(String timePrecision) {
        this.timePrecision = timePrecision;
    }

    public String getRetentionInterval() {
        return retentionInterval;
    }

    public void setRetentionInterval(String retentionInterval) {
        this.retentionInterval = retentionInterval;
    }

    public Map<String, String> getFields() {
        fields = new HashMap<>();
        if (enabled != null) {
            fields.put("enabled", enabled);
        }
        if (persistent != null) {
            fields.put("persistent", persistent);
        }
        if (dataType != null) {
            fields.put("dataType", dataType.toString());
        }
        if (timePrecision != null) {
            fields.put("timePrecision", timePrecision);
        }
        if (retentionInterval != null) {
            fields.put("retentionInterval", retentionInterval);
        }
        return fields;
    }
}
