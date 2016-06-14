package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.series.Metric;
import com.axibase.tsd.api.model.series.Query;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Map;

public class SeriesMethod extends Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String METHOD_SERIES_INSERT = "/series/insert";
    protected static final String METHOD_SERIES_QUERY = "/series/query";
    protected static final String METHOD_METRICS = "/metrics/";
    private JSONArray returnedSeries;

    protected Boolean insertSeries(final Series series) throws IOException {

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", series.getEntity());
                put("metric", series.getMetricName());
                put("data", new JSONArray() {{
                    add(new JSONObject() {{
                        ArrayList<Sample> data = series.getData();
                        for (Sample sample : data) {
                            put("d", sample.getD());
                            put("v", sample.getV());
                        }
                    }});
                }});
                put("tags", new JSONObject(series.getTags()));
            }});
        }};


        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_SERIES_INSERT, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Series looks inserted");
        } else {
            logger.error("Fail to insert series");
        }
        return 200 == response.getCode();
    }

    protected void executeQuery(final Query query) throws Exception {
        Thread.sleep(500);

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", query.getEntity());
                put("metric", query.getMetric());
                put("startDate", query.getStartDate());
                put("endDate", query.getEndDate());

            }});
        }};

        final AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_SERIES_QUERY, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Query looks succeeded");
        } else {
            logger.error("Failed to execute series query");
        }

        JSONParser parser = new JSONParser();

        returnedSeries = (JSONArray) parser.parse(response.getBody());
    }

    protected Boolean createOrReplaceMetric(String metric, Map body) throws IOException {
        JSONObject request = new JSONObject(body);
        AtsdHttpResponse response = httpSender.send(HTTPMethod.PUT, METHOD_METRICS + metric, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Metric looks created or replaced");
        } else {
            logger.error("Fail to create or replace metric");
        }
        return 200 == response.getCode();
    }

    protected Boolean createOrReplaceMetric(Metric metric) throws IOException {
        return createOrReplaceMetric(metric.getName(), metric.getParameters());
    }

    protected Boolean deleteMetric(String metric) throws IOException {
        AtsdHttpResponse response = httpSender.send(HTTPMethod.DELETE, METHOD_METRICS + metric, null);
        if (200 == response.getCode()) {
            logger.debug("Metric looks deleted");
        } else {
            logger.error("Fail to delete metric");
        }
        return 200 == response.getCode();
    }

    protected Boolean deleteMetric(Metric metric) throws IOException {
        return deleteMetric(metric.getName());
    }

    protected String getDataField(int index, String field) {
        if (returnedSeries == null) {
            return "";
        } else {
            return ((JSONObject) ((JSONArray) ((JSONObject) returnedSeries.get(0)).get("data")).get(index)).get(field).toString();
        }
    }
}
