package com.axibase.tsd.api.model.series;

import java.util.HashMap;
import java.util.Map;

public class Metric {
    private String name;
    private Map<String,String> parameters;

    public Metric(String name) {
        this.name = name;
        this.parameters = new HashMap();
    }

    public void setDataType(String dataType) {
        parameters.put("dataType", dataType);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
