package com.axibase.tsd.api.method.property.delete;


import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.builder.PropertyBuilder;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.propery.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * @author Dmitry Korchagin.
 */

@SuppressWarnings("unchecked")
public class PositiveTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DATASET_NAME = "positiveDelete.json";


    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }


    @Test
    public void propertyDelete_FutureDate_DeleteWithoutDateFilterByTypeAndEntity_ExactFALSE_PropertyDisappear() throws IOException {
        final Property  property = new PropertyBuilder().buildRandom();
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
    public void propertyDelete_FutureDate_DeleteExactWithoutDateFilter_PropertyDisappear() throws IOException {
        final Property  property = new PropertyBuilder().buildRandom();
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
    public void propertyDelete_CommonTypeAndEntity_DeleteByTypeEntityKey_OnlyFirstPropertyDisappear() throws IOException {
        final Property  property = new PropertyBuilder().buildRandom();
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
    public void propertyDelete_DeleteAllByType_exactFALSE_AllPropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
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
    public void propertyDelete_ByPropertyKey_PropertyDisappear() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
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
    public void checkDataset() throws IOException {

        JSONArray dataset = getDataset(DATASET_DIRECTORY + "/" + DATASET_NAME);
        assertNotNull(dataset);

        int size = dataset.size();
        logger.info("Starting to iterate Dataset ...");
        for (int i = 0; i < size; i++) {
            logger.info("Query set number: {}", i);
            JSONArray querySet = (JSONArray) dataset.get(i);
            logger.debug("query set: {}", querySet.toJSONString());

            JSONArray insert = (JSONArray) querySet.get(0);
            JSONArray query = (JSONArray) querySet.get(1);
            JSONArray deleteQuery = (JSONArray) querySet.get(2);

            logger.debug("insert: {}", insert);
            logger.debug("query: {}", query);
            logger.debug("deleteQuery: {}", deleteQuery);

            assertTrue(insertProperty(insert));
            assertTrue(propertyExist(query, insert));


            AtsdHttpResponse atsdResponse = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_DELETE, deleteQuery.toJSONString());
            assertEquals("HTTP Response code mismatch", 200, atsdResponse.getCode());

            assertFalse("Property still exist", propertyExist(query, insert));

        }

    }




}
