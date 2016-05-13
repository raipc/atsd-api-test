package com.axibase.tsd.api.model.propery;

//import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property extends PropertyKey {
    private Map<String, String> tags;
    private Long timestamp;

    public Property(){

    }

    public Property(String type, String entity, Map<String, String> key, Map<String, String> tags, Long timestamp) {
        super(type, entity, key);
        this.tags = tags;
        this.timestamp = timestamp;
    }


    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Property{" +
                "tags=" + tags +
                ", timestamp=" + timestamp +
                "} " + super.toString();
    }
}
