package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
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
        prepare();
    }

    @Test
    public void testFutureDateExactTRUE() throws IOException {
        final Property property = new Property("delete-type9", "delete-entity9");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getNextDay());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", true);

        deletePropertyCorrect(deleteObj);

        assertTrue("Property should be remain", propertyExist(property));
    }


    @Test
    public void testFutureDateExactFALSE() throws IOException {
        final Property property = new Property("delete-type8", "delete-entity8");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getNextDay());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", false);

        deletePropertyCorrect(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void testFutureDateExactDefault() throws IOException {
        final Property property = new Property("delete-type7", "delete-entity7");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        property.setDate(Util.getNextDay());
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("key", property.getKey());

        deletePropertyCorrect(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void testCommonTypeEntityTypeEntityKey() throws IOException {
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

        deletePropertyCorrect(deleteObj);

        assertFalse("First property should be deleted", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void testTypeEntityExactFalse() throws IOException {
        final Property property = new Property("delete-type-5", "delete-entity5");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Property secondProperty = new Property();
        secondProperty.setType(property.getType());
        secondProperty.setEntity(property.getEntity());
        secondProperty.setTags(property.getTags());
        secondProperty.addKey("k2", "v2");
        insertPropertyCheck(secondProperty);
        logger.info("secondProperty inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", false);

        deletePropertyCorrect(deleteObj);

        assertFalse("Frist property should be deleted", propertyExist(property));
        assertFalse("Second property should be deleted", propertyExist(secondProperty));

    }


    @Test
    public void testTypeEntityKey() throws IOException {
        final Property property = new Property("delete-type4", "delete-entity4");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("key", property.getKey());

        deletePropertyCorrect(deleteObj);

        assertFalse("Property should be deleted", propertyExist(property));
    }

    @Test
    public void testMultipleTypeEntityExactTRUE() throws IOException {
        final Property property = new Property("delete-type3", "delete-entity3");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Property secondProperty = new Property();
        secondProperty.setType(property.getType());
        secondProperty.setEntity(property.getEntity());
        secondProperty.addKey("k2", "v2");
        secondProperty.setTags(property.getTags());
        insertPropertyCheck(secondProperty);
        logger.info("Second property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("type", property.getType());
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("exactMatch", true);

        deletePropertyCorrect(deleteObj);
        assertTrue("First property should remain", propertyExist(property));
        assertTrue("Second property should remain", propertyExist(secondProperty));
    }


    @Test
    public void testTypeEntityExactTRUE() throws IOException {
        final Property property = new Property("delete-type2", "delete-entity2");
        property.addTag("t1", "v1");
        property.addKey("k1", "v1");
        insertPropertyCheck(property);
        logger.info("Property inserted");

        Map<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("entity", property.getEntity());
        deleteObj.put("type", property.getType());
        deleteObj.put("exactMatch", true);

        deletePropertyCorrect(deleteObj);

        assertTrue("Property should be remain", propertyExist(property));
    }

    @Test
    public void testEndDateEqDate() throws IOException {
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

        deletePropertyCorrect(deleteObj);

        assertTrue("Property should be remain", propertyExist(property));
    }

    @Test
    public void test_TypeStartEnd() throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("type", "testtype");
        request.put("startDate", "2016-06-01T12:04:59.191Z");
        request.put("endDate", "2016-06-01T12:04:59.191Z");


        AtsdHttpResponse response = deleteProperty(request);
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void testTypeEnd() throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("type", "testtype");
        request.put("endDate", "2016-06-01T12:04:59.191Z");

        AtsdHttpResponse response = deleteProperty(request);
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void testTypeStart() throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("type", "testtype");
        request.put("startDate", "2016-06-01T12:04:59.191Z");

        AtsdHttpResponse response = deleteProperty(request);
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    @Test
    public void testTypeException() throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("type", "testtype");


        AtsdHttpResponse response = deleteProperty(request);
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Entity is required\"}", response.getBody());
    }

    private void deletePropertyCorrect(final Map deleteObj) throws IOException {
        AtsdHttpResponse response = super.deleteProperty(deleteObj);
        assertEquals("Fail to execute delete query", 200, response.getCode());
    }


}
