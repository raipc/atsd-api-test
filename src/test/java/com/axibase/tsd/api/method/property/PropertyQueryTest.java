package com.axibase.tsd.api.method.property;


import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Korchagin.
 */

@SuppressWarnings("unchecked")
public class PropertyQueryTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @BeforeClass
    public static void setUpBeforeClass() {
        prepare();
    }


    @Test
    public void test_TypeEntitiesStartEnd_bothFounded_wrongNotFounded() throws Exception {
        final Property property = new Property("query-type9", "query-entity9");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        insertPropertyCheck(property);

        final Property lastProperty = new Property(null, "query-entity9-2");
        lastProperty.setType(property.getType());
        lastProperty.addTag("t2", "tv2");


        insertPropertyCheck(lastProperty);

        final Property wrongProperty = new Property(null, "query-wrongentity-9");
        wrongProperty.setType(property.getType());
        wrongProperty.addTag("tw1", "twv1");
        insertPropertyCheck(wrongProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entities", new ArrayList<String>() {{
            add(property.getEntity());
            add(lastProperty.getEntity());
        }});
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", Util.getMaxDate());

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
            add(lastProperty);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_TypeEntityStartEnd_LastDEFAULT_bothFounded() throws Exception {
        final Property property = new Property("query-type8", "query-entity8");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPastDate());
        insertPropertyCheck(property);

        final Property lastProperty = new Property();
        lastProperty.setType(property.getType());
        lastProperty.setEntity(property.getEntity());
        lastProperty.addTag("t2", "tv2");
        lastProperty.setDate(Util.getCurrentDate());
        insertPropertyCheck(lastProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", Util.getMaxDate());

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
            add(lastProperty);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_TypeEntityStartEnd_LastFALSE_bothFounded() throws Exception {
        final Property property = new Property("query-type7", "query-entity7");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPastDate());
        insertPropertyCheck(property);

        final Property lastProperty = new Property();
        lastProperty.setType(property.getType());
        lastProperty.setEntity(property.getEntity());
        lastProperty.addTag("t1l", "tv1l");
        lastProperty.setDate(Util.getCurrentDate());
        insertPropertyCheck(lastProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", Util.getMaxDate());
        queryObj.put("last", false);

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
            add(lastProperty);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
        logger.debug("expected: {}", expected);
        logger.debug("given: {}", queryProperty(queryObj));
    }

    @Test
    public void test_TypeEntityStartEnd_LastTRUE_LastFounded() throws Exception {
        final Property property = new Property("query-type6", "query-entity6");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPastDate());
        insertPropertyCheck(property);

        final Property lastProperty = new Property();
        lastProperty.setType(property.getType());
        lastProperty.setEntity(property.getEntity());
        lastProperty.addTag("t1l", "tv1l");
        lastProperty.setDate(Util.getCurrentDate());
        insertPropertyCheck(lastProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", Util.getMaxDate());
        queryObj.put("last", true);

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(lastProperty);
        }});
        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
        expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});
        JSONAssert.assertNotEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_TypeEntity_StartPast_IntervalGiveFuture_propertyFounded() throws Exception {
        final Property property = new Property("query-type5", "query-entity5");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("key", property.getKey());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("interval", new JSONObject() {{
            put("count", 999);
            put("unit", "YEAR");
        }});

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});
        logger.debug("Expected json: {}", expected);
        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void test_TypeEntityEnd_StartEQDate_Interval1MS_propertyFounded() throws Exception {
        final Property property = new Property("query-type4", "query-entity4");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", property.getDate());
        queryObj.put("endDate", Util.getMaxDate());

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void test_TypeEntityKey_StartPast_EndFuture_propertyFounded() throws Exception {
        final Property property = new Property("query-type3", "query-entity3");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("key", property.getKey());
        queryObj.put("startDate", Util.ISOFormat(Util.getPastDate()));
        queryObj.put("endDate", Util.ISOFormat(Util.getFutureDate()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void test_TypeEntityStartEnd_ExactDEFAULT_propertyFounded() throws Exception {
        final Property property = new Property("query-type2", "query-entity2");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", property.getDate());
        queryObj.put("endDate", Util.ISOFormat(Util.getFutureDate()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_TypeEntityStartEnd_ExactFALSE_propertyFounded() throws Exception {
        final Property property = new Property("query-type1", "query-entity1");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("exactMatch", false);
        queryObj.put("startDate", property.getDate());
        queryObj.put("endDate", Util.ISOFormat(Util.getFutureDate()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_Example_TypeEntityStartEnd_Partkey_propertyFounded() throws Exception {
        final Property property = new Property("disk", "nurswgvml007");
        property.addTag("fs_type", "ext4");
        property.addKey("file_system", "/");
        property.addKey("mount_point", "/sda1");
        property.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse("2016-05-25T04:00:00.000Z"));
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("key", new HashMap<String, String>() {{
            put("file_system", "/");
        }});
        queryObj.put("startDate", "2016-05-25T04:00:00Z");
        queryObj.put("endDate", "2016-05-25T05:00:00Z");

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void test_TypeEntity_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("entity", "testentity");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Missing parameters. One of the following combinations is required: interval, interval + startTime/startDate, interval + endTime/endDate, startTime/startDate + endTime/endDate\"}", response.getBody());
    }

    @Test
    public void test_Type_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Missing parameters. One of the following combinations is required: interval, interval + startTime/startDate, interval + endTime/endDate, startTime/startDate + endTime/endDate\"}", response.getBody());
    }

    @Test
    public void test_TypeStart_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("startDate", "2016-06-01T12:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Missing parameters. One of the following combinations is required: interval, interval + startTime/startDate, interval + endTime/endDate, startTime/startDate + endTime/endDate\"}", response.getBody());
    }

    @Test
    public void test_TypeStartEnd_Exception() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("startDate", "2016-06-01T12:04:59.191Z");
                put("endDate", "2016-06-01T13:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: entity or entities or entityGroup or entityExpression must not be empty\"}", response.getBody());
    }


}
