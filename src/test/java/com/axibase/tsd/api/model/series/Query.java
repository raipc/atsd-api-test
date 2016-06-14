package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.Util;

public class Query {
    private String entity;
    private String metric;
    private String startDate;
    private String endDate;

    public Query(String entity, String metric, long startTime, long endTime) {
        this.entity = entity;
        this.metric = metric;
        this.startDate = Util.ISOFormat(startTime);
        this.endDate = Util.ISOFormat(endTime);
    }

    public Query(String entity, String metric, String startDate, String endDate) {
        this.entity = entity;
        this.metric = metric;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
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

    @Override
    public String toString() {
        return "Query{" +
                "entity='" + entity + '\'' +
                ", metric='" + metric + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
