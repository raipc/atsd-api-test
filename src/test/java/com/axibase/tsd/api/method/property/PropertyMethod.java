package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Korchagin.
 */
@SuppressWarnings("unchecked")
abstract public class PropertyMethod extends Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static final String METHOD_PROPERTY_INSERT = "/properties/insert";
    static final String METHOD_PROPERTY_QUERY = "/properties/query";
    static final String METHOD_PROPERTY_DELETE = "/properties/delete";

    void insertPropertyCheck(final Property property) throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                if(property.getKey() != null) {
                    put("key", property.getKey());
                }
                put("tags", property.getTags());
                if(property.getDate() != null) {
                    put("date", property.getDate());
                }
            }});
        }};
        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_INSERT, request.toJSONString());
        if (response.getCode() != 200) {
            throw new IOException("Fail to insert property");
        }
        if (!propertyExist(property)) {
            throw new IOException("Fail to check inserted property");
        }
    }


    Boolean propertyExist(final Property property) throws IOException {

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
                put("startDate", (property.getDate() == null?"1970-01-01T00:00:00.000Z":property.getDate()));
                put("endDate", "9999-01-12T13:46:40.000Z");
            }});
        }};

        String propertyJson =  jacksonMapper.writeValueAsString(new ArrayList<Property>(){{ add(property);}});

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(200, response.getCode());
        logger.debug("check: {}\nresponse: {}", propertyJson, response.getBody());

        try {
            JSONAssert.assertEquals(propertyJson, response.getBody(), JSONCompareMode.LENIENT);
        } catch (JSONException e) {
            throw new IOException("Can not deserialize response");
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    String queryProperty(final Map request) throws IOException {

        JSONArray query = new JSONArray() {{
            add(new JSONObject(request));
        }};

        AtsdHttpResponse atsdResponse = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, query.toJSONString());
        assertEquals(200, atsdResponse.getCode());

        return atsdResponse.getBody();
    }

    private JSONArray buildJsonArray(final Property... properties) {
        JSONArray jsonArray = new JSONArray();
        for (final Property property : properties) {
            jsonArray.add(buildJsonObject(property));
        }
        return jsonArray;
    }

    private JSONObject buildJsonObject(final Property property) {
        return new JSONObject() {{
            put("entity", property.getEntity());
            put("type", property.getType());
            if(property.getKey() != null) {
                put("key", property.getKey());
            }
            put("tags", property.getTags());
            if(property.getDate() != null) {
                put("date", property.getDate());
            }
        }};
    }
}
