package com.axibase.tsd.api.method.property.delete;


import com.axibase.tsd.api.builder.PropertyBuilder;
import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.propery.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dmitry Korchagin.
 */

public class RandomPositiveTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Property property;

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }

    @Before
    public void prepareProperty() throws IOException {
        property = new PropertyBuilder().buildRandom();
        logger.debug("Generated property: {}", property.toString());
        if (!insertProperty(property) || propertyExist(property)) {
            fail();
        }
    }


    @Test
    public void propertyDelete_ByPropertyKey_PropertyDisappear() throws IOException {
        logger.info("Property inserted");
        JSONArray request = new JSONArray() {{
            add(new JSONObject(){{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());
        if (propertyExist(property)) {
            fail();
        }
        logger.info("property deleted");

    }

    @Test
    public void propertyDelete_DeleteAllByType_AllPropertyDisappear() throws IOException {
        logger.info("Property inserted");
        Property secondProperty = new PropertyBuilder().buildRandom();
        secondProperty.setType(property.getType());

        logger.debug("Generated property: {}", secondProperty.toString());
        if (!insertProperty(property) || propertyExist(property)) {
            fail();
        }
        logger.info("Second property inserted");

        JSONArray request = new JSONArray() {{
            add(new JSONObject(){{
                put("type", property.getType());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());
        if (propertyExist(property) || propertyExist(secondProperty)) {
            fail();
        }
        logger.info("both properties deleted");

    }


    private Boolean insertProperty(final Property property) throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject(){{
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
            logger.error("Fail to insert propertry");
        }
        return 200 == response.getCode();
    }



    private Boolean propertyExist(final Property property) throws IOException {

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("interval", new JSONObject() {{
                    put("count", 5);
                    put("unit", "MINUTE");
                }});
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(200, response.getCode());

        JSONArray responseBody;
        try {
            responseBody = (JSONArray) new JSONParser().parse(response.getBody());
        } catch (ParseException e) {
            logger.error("Fail to parse response body: {}", response.getBody());
            return null;
        }

        return responseBody.contains(property);
    }

}
