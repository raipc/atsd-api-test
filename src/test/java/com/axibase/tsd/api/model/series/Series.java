package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.model.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Series extends Model {
    private String entity;
    private String metric;
    private ArrayList<Sample> data;
    private Map<String, String> tags;

    public Series(String entity, String metric) {
        this.entity = entity;
        this.metric = metric;
        this.data = new ArrayList<>();
        this.tags = new HashMap<>();
    }

    public String getEntity() {
        return entity;
    }

    public String getMetricName() {
        return metric;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public ArrayList<Sample> getData() {
        return data;
    }

    public void setData(ArrayList<Sample> data) {
        this.data = data;
    }

    public void setData(Sample sample) {
        data = new ArrayList<>();
        addData(sample);
    }

    public void addTag(String key, String value){
        tags.put(key, value);
    }

    public void addData(Sample sample){
        data.add(sample);
    }

    @Override
    public String toString() {
        return "Series{" +
                "entity='" + entity + '\'' +
                ", metric='" + metric + '\'' +
                ", tags=" + tags +
                ", data=" + data +
                '}';
    }
}
