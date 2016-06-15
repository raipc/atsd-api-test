package com.axibase.tsd.api.method.series;

import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.series.Sample;
import com.axibase.tsd.api.model.series.Series;
import com.axibase.tsd.api.model.series.SeriesQuery;
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

public class SeriesMethod extends Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String METHOD_SERIES_INSERT = "/series/insert";
    protected static final String METHOD_SERIES_QUERY = "/series/query";
    private JSONArray returnedSeries;
    private JSONParser jsonParser = new JSONParser();

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

    protected Boolean executeQuery(final SeriesQuery seriesQuery) throws Exception {
        Thread.sleep(500);

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", seriesQuery.getEntity());
                put("metric", seriesQuery.getMetric());
                put("startDate", seriesQuery.getStartDate());
                put("endDate", seriesQuery.getEndDate());
                put("tags", seriesQuery.getTags());

            }});
        }};

        final AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_SERIES_QUERY, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Query looks succeeded");
        } else {
            logger.error("Failed to execute series query");
        }
        returnedSeries = (JSONArray) jsonParser.parse(response.getBody());
        return 200 == response.getCode();
    }


    protected String getDataField(int index, String field) {
        if (returnedSeries == null) {
            return "";
        }
        return ((JSONObject) ((JSONArray) ((JSONObject) returnedSeries.get(0)).get("data")).get(index)).get(field).toString();
    }
}
