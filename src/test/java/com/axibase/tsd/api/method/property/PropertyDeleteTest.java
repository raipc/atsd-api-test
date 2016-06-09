package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.builder.PropertyBuilder;
import com.axibase.tsd.api.model.propery.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import static org.junit.Assert.*;

/**
 * @author Dmitry Korchagin.
 */
@SuppressWarnings("unchecked")
public class PropertyDeleteTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
        setUpRegistry();
    }


    @Test
    public void test_FutureDate_DeleteWithoutDateFilterByTypeAndEntity_ExactFALSE_PropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e8"));
        property.setType(registry.registerType("t8"));
        property.setDate(Util.format(Util.getFutureDate()));
        logger.debug("Property generated : {}", property.toString());

        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");


        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("exactMatch", false);
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_FutureDate_DeleteExactWithoutDateFilter_PropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e7"));
        property.setType(registry.registerType("t7"));
        property.setDate(Util.format(Util.getFutureDate()));
        logger.debug("Property generated : {}", property.toString());

        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");


        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("key", property.getKey());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_CommonTypeAndEntity_ByPropertyKey_OnlyFirstPropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e6"));
        property.setType(registry.registerType("t6"));
        logger.debug("First property generated : {}", property.toString());

        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("First property inserted");


        Property secondProperty = new PropertyBuilder().buildRandom();
        secondProperty.setType(property.getType());

        logger.debug("Generated property: {}", secondProperty.toString());
        if (!insertProperty(secondProperty)) {
            fail("Fail to insert secondProperty");
        }
        if (!propertyExist(secondProperty)) {
            fail("Fail to check secondProperty insert");
        }
        logger.info("Second property inserted");

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("key", property.getKey());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertFalse("First property should be deleted", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void test_DeleteAllByType_exactFALSE_AllPropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e5"));
        property.setType(registry.registerType("t5"));
        logger.debug("First property generated : {}", property.toString());

        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("First property inserted");

        Property secondProperty = new PropertyBuilder().buildRandom();
        secondProperty.setType(property.getType());
        secondProperty.setEntity(property.getEntity());
        logger.debug("Second property generated : {}", secondProperty.toString());
        if (!insertProperty(secondProperty) || !propertyExist(secondProperty)) {
            fail("Fail to insert secondProperty");
        }
        logger.info("Second property inserted");

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("exactMatch", false);
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());
        assertFalse("Frist property should be deleted", propertyExist(property));
        assertFalse("Second property should be deleted", propertyExist(secondProperty));

    }


    @Test
    public void test_ByPropertyKey_PropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e4"));
        property.setType(registry.registerType("t4"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_DeleteByTypeAndEntity_exactTRUE_PropertiesRemain() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e3"));
        property.setType(registry.registerType("t3"));
        logger.debug("First property generated : {}", property.toString());

        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("First property inserted");
        Property secondProperty = new PropertyBuilder().buildRandom();
        secondProperty.setType(property.getType());
        secondProperty.setEntity(property.getEntity());

        logger.debug("Generated property: {}", secondProperty.toString());
        if (!insertProperty(secondProperty)) {
            fail("Fail to insert secondProperty");
        }
        if (!propertyExist(secondProperty)) {
            fail("Fail to check secondProperty insert");
        }
        logger.info("Second property inserted");

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("exactMatch", true);
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertTrue("First property should remain", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void test_ByTypeAndEntity_exactTRUE_PropertyRemain() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e2"));
        property.setType(registry.registerType("t2"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("exactMatch", true);
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertTrue("Property should be remain", propertyExist(property));
    }

    @Test
    public void test_ByPropertyKey_EndDateEQDate_PropertyRemain() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setEntity(registry.registerEntity("e1"));
        property.setType(registry.registerType("t1"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("key", property.getKey());
                put("endDate", property.getDate());
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(200, response.getCode());

        assertTrue("Property should be remain", propertyExist(property));
    }

    @Test
    public void test_TypeStartEnd_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("startDate", "2016-06-01T12:04:59.191Z");
                put("endDate", "2016-06-01T12:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void test_TypeEnd_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("endDate", "2016-06-01T12:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void test_TypeStart_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("startDate", "2016-06-01T12:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void test_Type_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }


}
