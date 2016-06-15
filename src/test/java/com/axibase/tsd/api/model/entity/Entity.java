package com.axibase.tsd.api.model.entity;

import com.axibase.tsd.api.model.Model;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastInsertDate() {
        return lastInsertDate;
    }

    public void setLastInsertDate(Date lastInsertDate) {
        this.lastInsertDate = lastInsertDate;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}
