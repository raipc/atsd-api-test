package com.axibase.tsd.api.model.propery;

import com.axibase.tsd.api.model.Model;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
abstract public class PropertyKey extends Model {
    private String type;
    private String entity;
    private Map<String, String> key;

    public PropertyKey(){

    }


    protected PropertyKey(String entity, String type, Map<String, String> key) {
        this.entity = entity;
        this.type = type;
        this.key = key;
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

    @Override
    public String toString() {
        return "PropertyKey{" +
                "type='" + type + '\'' +
                ", entity='" + entity + '\'' +
                ", key=" + key +
                '}';
    }
}
