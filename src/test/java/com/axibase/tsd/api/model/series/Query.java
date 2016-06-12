package com.axibase.tsd.api.model.series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Query {
    private String entity;
    private String metric;
    private String startDate;
    private String endDate;

    public Query(String entity, String metric, long startTime, long endTime) {
        this.entity = entity;
        this.metric = metric;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        this.startDate = dateFormat.format(calendar.getTime());
        calendar.setTimeInMillis(endTime);
        this.endDate = dateFormat.format(calendar.getTime());
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
