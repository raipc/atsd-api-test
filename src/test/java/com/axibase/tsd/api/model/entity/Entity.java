package com.axibase.tsd.api.model.entity;

import com.axibase.tsd.api.model.Model;
import com.axibase.tsd.api.registry.EntityRegistry;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

/**
 * @author Dmitry Korchagin.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entity extends Model {
    private String name;
    private Date lastInsertDate;
    private Map<String, String> tags;

    public Entity() {

    }

    public Entity(String name) {
        if(null != name) {
            EntityRegistry.getInstance().registerEntity(name);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastInsertDate() {
        if(null == lastInsertDate) {
            return null;
        }
        return (Date)lastInsertDate.clone();
    }

    public void setLastInsertDate(Date lastInsertDate) {
        this.lastInsertDate = lastInsertDate;
    }

    public Map<String, String> getTags() {
        return new HashMap<>(tags);
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
