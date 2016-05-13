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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dmitry Korchagin.
 */

public class BatchDeleteTest extends Method {
    private static final String ATSD_METHOD = "/properties";
    private static final Logger logger = LoggerFactory.getLogger(BatchDeleteTest.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }


    @Test
    @SuppressWarnings("unchecked")
    public void batchPropertyDelete_CorrectPropertyDelete_PropertyDisappear() throws IOException {
        final String type = Util.buildVariablePrefix() + "type";
        final String entity = Util.buildVariablePrefix() + "entity";
        final Long timestamp = System.currentTimeMillis();

        final JSONObject propertyKey = new JSONObject() {{
            put("type", type);
            put("entity", entity);
            put("key", new JSONObject() {{
                put("key1", "keyval1");
                put("key2", "keyval2");
            }});
        }};

        final JSONObject property = new JSONObject() {{
            put("type", type);
            put("entity", entity);
            put("key", new JSONObject() {{
                put("key1", "keyval1");
                put("key2", "keyval2");
            }});
            put("tags", new JSONObject() {{
                put("tag1", "tagval1");
                put("tag2", "tagval2");
            }});
            put("timestamp", timestamp);
        }};


        {
            JSONArray insertPropertyRequest = new JSONArray() {{
                add(property);
            }};
            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD + "/insert", insertPropertyRequest.toString());
            assertEquals(200, response.getCode());
        }



        JSONObject getPropertyRequest = new JSONObject() {{
            put("queries", new JSONArray() {{
                add(new JSONObject() {{
                    put("type", type);
                    put("entity", entity);
                    put("key", new JSONObject() {{
                        put("key1", "keyval1");
                        put("key2", "keyval2");
                    }});
                }});
            }});
        }};

        {

            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, getPropertyRequest.toJSONString());
            assertEquals(200, response.getCode());
            try {
                assertEquals(new JSONArray(){{add(property);}}, new JSONParser().parse(response.getBody()));
            } catch (org.json.simple.parser.ParseException e) {
                fail();
            }
        }

        {
            JSONArray deletePropertyRequest = new JSONArray() {{
                add(new JSONObject() {{
                    put("action", "delete");
                    put("properties", new JSONArray() {{
                        add(new JSONObject(){{
                            put("type", type);
                            put("entity", entity);
                            put("key", new JSONObject() {{
                                put("key1", "keyval1");
                                put("key2", "keyval2");
                            }});
                        }});
                    }});
                }});
            }};

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
