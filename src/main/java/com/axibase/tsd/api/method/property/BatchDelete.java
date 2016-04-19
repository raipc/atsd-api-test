package com.axibase.tsd.api.method.property;


import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * @author Dmitry Korchagin.
 */

public class BatchDelete extends Method {
    private static final String ATSD_METHOD = "/properties";
    private static final Logger logger = LoggerFactory.getLogger(BatchDelete.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }


    @Test
    public void batchPropertyDelete_CorrectPropertyDelete_PropertyDisappear() throws IOException {
        final String type = Util.buildVariablePrefix() + "type";
        final String entity = Util.buildVariablePrefix() + "entity";
        final Long timestamp = System.currentTimeMillis();

        JSONObject key = new JSONObject();
        key.put("key1", "keyval1");
        JSONObject tags = new JSONObject();
        tags.put("tag1", "tagval1");
        JSONObject propertyKey = new JSONObject();
        propertyKey.put("type", type);
        propertyKey.put("entity", entity);
        propertyKey.put("key", key);

        JSONObject property = (JSONObject) propertyKey.clone();
        property.put("tags", tags);
        property.put("timestamp", timestamp);

        JSONArray insertPropertyRequest = new JSONArray();
        insertPropertyRequest.add(property);

        JSONObject getPropertyRequest = new JSONObject();

        try {
            getPropertyRequest.put("queries", new JSONParser().parse("[" + propertyKey + "]"));
        } catch (org.json.simple.parser.ParseException e) {
            logger.error("fail to prepare test data");
            e.printStackTrace();
            fail();
        }
        JSONArray deletePropertyRequest = new JSONArray();
        JSONObject DP = new JSONObject();
        DP.put("action", "delete");
        try {
            DP.put("properties", new JSONParser().parse("[" + propertyKey + "]"));
        } catch (org.json.simple.parser.ParseException e) {
            logger.error("fail to prepare test data");
            e.printStackTrace();
            fail();
        }
        deletePropertyRequest.add(DP);


        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD + "/insert", insertPropertyRequest.toString());
            assertEquals(200, response.getCode());
        }

        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, getPropertyRequest.toJSONString());
            assertEquals(200, response.getCode());
            try {
                assertEquals(new JSONParser().parse("[" + property + "]"), new JSONParser().parse(response.getBody()));
            } catch (org.json.simple.parser.ParseException e) {
                fail();
            }
        }

        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.PATH, ATSD_METHOD, deletePropertyRequest.toString());
            assertEquals(200, response.getCode());

            response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, getPropertyRequest.toString());
            assertEquals(200, response.getCode());
            try {
                assertEquals(new JSONArray(), new JSONParser().parse(response.getBody()));
            } catch (org.json.simple.parser.ParseException e) {
                fail();
            }
        }

    }

}
