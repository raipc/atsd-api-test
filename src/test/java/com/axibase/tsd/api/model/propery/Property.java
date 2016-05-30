package com.axibase.tsd.api.model.propery;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class Property extends PropertyKey {
    private Map<String, String> tags;
    private String date;

    public Property(){

    }

    public Property(String type, String entity, Map<String, String> key, Map<String, String> tags, String date) {
        super(type, entity, key);
        this.tags = tags;
        this.date = date;
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
