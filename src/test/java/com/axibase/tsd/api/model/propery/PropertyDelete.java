package com.axibase.tsd.api.model.propery;

//import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDelete extends PropertyKey {
    private Long createBeforeTime;
    private String createBeforeDate;

    public PropertyDelete() {

    }

    public PropertyDelete(String type, String entity, Map<String, String> key, Long createBeforeTime) {
        this(type, entity, key, createBeforeTime, null);
    }

    public PropertyDelete(String type, String entity, Map<String, String> key, String createBeforeDate) {
        this(type, entity, key, null, createBeforeDate);
    }

    public PropertyDelete(String type, String entity, Map<String, String> key, Long createBeforeTime, String createBeforeDate) {
        super(type, entity, key);
        this.createBeforeTime = createBeforeTime;
        this.createBeforeDate = createBeforeDate;
    }


    public Long getCreateBeforeTime() {
        return createBeforeTime;
    }

    public void setCreateBeforeTime(Long createBeforeTime) {
        this.createBeforeTime = createBeforeTime;
    }

    public String getCreateBeforeDate() {
        return createBeforeDate;
    }

    public void setCreateBeforeDate(String createBeforeDate) {
        this.createBeforeDate = createBeforeDate;
    }

    @Override
    public String toString() {
        return "PropertyDelete{" +
                "createBeforeTime=" + createBeforeTime +
                ", createBeforeDate='" + createBeforeDate + '\'' +
                "} " + super.toString();
    }
}