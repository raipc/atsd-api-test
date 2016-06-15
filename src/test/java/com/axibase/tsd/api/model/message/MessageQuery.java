package com.axibase.tsd.api.model.message;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageQuery {
    private String entity;
    private String startDate;
    private String endDate;

    private String type;
    private Map<String, String> tags;

    public MessageQuery(String entity, String startDate, String endDate) {
        this.entity = entity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

        public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
