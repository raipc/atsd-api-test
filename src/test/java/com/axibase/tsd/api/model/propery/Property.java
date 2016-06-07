package com.axibase.tsd.api.model.propery;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class Property {
    private String type;
    private String entity;
    private Map<String, String> key;
    private Map<String, String> tags;
    private String date;

    public Property() {
    }

    public Property(String type, String entity, Map<String, String> key, Map<String, String> tags, String date) {
        this.type = type;
        this.entity = entity;
        this.key = key;
        this.tags = tags;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Map<String, String> getKey() {
        return key;
    }

    public void setKey(Map<String, String> key) {
        this.key = key;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + getType() + '\'' +
                ", entity='" + getEntity() + '\'' +
                ", key=" + getKey() +
                ", tags=" + getTags() +
                ", date=" + getDate() +
                '}';
    }
}
