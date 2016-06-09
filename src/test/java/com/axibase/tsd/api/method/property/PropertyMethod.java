package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.propery.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Korchagin.
 */
@SuppressWarnings("unchecked")
abstract public class PropertyMethod extends Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final String METHOD_PROPERTY_INSERT = "/properties/insert";
    protected static final String METHOD_PROPERTY_QUERY = "/properties/query";
    protected static final String METHOD_PROPERTY_DELETE = "/properties/delete";

    protected Boolean insertProperty(final Property property) throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
                put("tags", property.getTags());
                put("date", property.getDate());
            }});
        }};
        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_INSERT, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Property looks inserted");
        } else {
            logger.error("Fail to insert property");
        }
        return 200 == response.getCode();
    }

    protected Boolean insertProperty(final JSONArray request) throws IOException {

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_INSERT, request.toJSONString());
        if (200 == response.getCode()) {
            logger.debug("Property looks inserted");
        } else {
            logger.error("Fail to insert property");
        }
        return 200 == response.getCode();
    }

    protected Boolean propertyExist(final Property property) throws IOException {

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
                put("startDate", property.getDate());
                put("interval", new JSONObject() {{
                    put("count", 1);
                    put("unit", "MILLISECOND");
                }});
            }});
        }};

        JSONObject propertyObject = new JSONObject() {{
            put("entity", property.getEntity());
            put("type", property.getType());
            put("key", property.getKey());
            put("tags", property.getTags());
            put("date", property.getDate());

        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(200, response.getCode());

        JSONArray responseBody;
        try {
            responseBody = (JSONArray) new JSONParser().parse(response.getBody());
        } catch (ParseException e) {
            logger.error("Fail to parse response body: {}", response.getBody());
            return false;
        }
        logger.debug("check: {}\nresponse: {}", propertyObject, responseBody);
        return responseBody.contains(propertyObject);
    }

    protected Boolean propertiesExist(final JSONArray request, final JSONArray properties) throws IOException {


        AtsdHttpResponse atsdResponse = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(200, atsdResponse.getCode());

        JSONArray responseBody;
        try {
            responseBody = (JSONArray) new JSONParser().parse(atsdResponse.getBody());
        } catch (ParseException e) {
            logger.error("Fail to parse response body: {}", atsdResponse.getBody());
            return false;
        }
        logger.debug("check: {}\nresponse: {}", properties, responseBody);
        return responseBody.containsAll(properties);
    }

    protected JSONArray buildJsonArray(final Property... properties) {
        JSONArray jsonArray = new JSONArray();
        for(final Property property: properties) {
            jsonArray.add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
                put("tags", property.getTags());
                put("date", property.getDate());
            }});
        }
        return jsonArray;
    }
}
