package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.builder.PropertyBuilder;
import com.axibase.tsd.api.model.property.Property;
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
import java.util.HashMap;
import java.util.Map;

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
    }

    @Test
    public void test_FutureDate_TypeEntity_ExactTRUE_PropertyRemain() throws IOException {
        final Property property = new Property("delete-type9", "delete-entity9");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getFutureDate());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", true);

        deleteProperty(deleteObj);

        assertTrue("Property should be remain", propertyExist(property));
    }



    @Test
    public void test_FutureDate_TypeEntity_ExactFALSE_PropertyDisappear() throws IOException {
        final Property property = new Property("delete-type8", "delete-entity8");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getFutureDate());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", false);

        deleteProperty(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_FutureDate_TypeEntityKey_PropertyDisappear() throws IOException {
        final Property property = new Property("delete-type7", "delete-entity7");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getFutureDate());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("key", property.getKey());

        deleteProperty(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_CommonTypeEntity_TypeEntityKey_FirstDisappear() throws IOException {
        final Property property = new Property("delete-type-6", "delete-entity6");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Property secondProperty = new Property(null, "delete-entity6-2");
        secondProperty.setType(property.getType());
        secondProperty.setTags(property.getTags());
        secondProperty.addKey("k2", "v2");
        insertPropertyCheck(secondProperty);
        logger.info("secondProperty inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("key", property.getKey());

        deleteProperty(deleteObj);

        assertFalse("First property should be deleted", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void test_TypeEntity_ExactFALSE_PropertiesDisappear() throws IOException {
        final Property property = new Property("delete-type-5", "delete-entity5");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Property secondProperty = new Property(property.getType(), property.getEntity());
        secondProperty.setTags(property.getTags());
        secondProperty.addKey("k2", "v2");
        insertPropertyCheck(secondProperty);
        logger.info("secondProperty inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", false);

        deleteProperty(deleteObj);

        assertFalse("Frist property should be deleted", propertyExist(property));
        assertFalse("Second property should be deleted", propertyExist(secondProperty));

    }


    @Test
    public void test_TypeEntityKey_PropertyDisappear() throws IOException {
        final Property property = new Property("delete-type4", "delete-entity4");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("key", property.getKey());

        deleteProperty(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void test_TypeEntity_exactTRUE_PropertiesRemain() throws IOException {
        final Property property = new Property("delete-type3", "delete-entity3");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Property secondProperty = new Property(property.getType(), property.getEntity());
        secondProperty.addKey("k2", "v2");
        secondProperty.setTags(property.getTags());
        insertPropertyCheck(secondProperty);
        logger.info("Second property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", true);

        deleteProperty(deleteObj);
        assertTrue("First property should remain", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void test_TypeEntity_exactTRUE_PropertyRemain() throws IOException {
        final Property property = new Property("delete-type2", "delete-entity2");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("type", property.getType());
        deleteObj.put("exactMatch", true);

        deleteProperty(deleteObj);

        assertTrue("Property should be remain", propertyExist(property));
    }

    @Test
    public void test_TypeEntityKey_EndDateEQDate_PropertyRemain() throws IOException {
        final Property property = new Property("delete-type1", "delete-entity1");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(System.currentTimeMillis());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("type", property.getType());
        deleteObj.put("key", property.getKey());
        deleteObj.put("endDate", property.getDate());

        deleteProperty(deleteObj);

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

    private void deleteProperty(final Map deleteObj) throws IOException {
        JSONArray jsonArray = new JSONArray(){{
            add(new JSONObject(deleteObj));
        }};
        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, jsonArray.toJSONString());
        assertEquals("Fail to execute delete query", 200, response.getCode());
    }


}
