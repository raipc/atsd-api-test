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
    public void testExactFalseWildcartNoKey() throws Exception {
        final Property property = new Property("query-type18", "query-entity18");
        property.addTag("t1", "tv1");
        insertPropertyCheck(property);

        final Property secondProperty = new Property(null, "query-entity18-2");
        secondProperty.setType(property.getType());
        secondProperty.addTag("t2", "tv2");
        secondProperty.addKey("k2", "kv2");
        insertPropertyCheck(secondProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", "*");
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("exactMatch", false);


        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
            add(secondProperty);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void testExactTrueWildcartNoKey() throws Exception {
        final Property property = new Property("query-type17", "query-entity17");
        property.addTag("t1", "tv1");
        insertPropertyCheck(property);

        final Property secondProperty = new Property(null, "query-entity17-2");
        secondProperty.setType(property.getType());
        secondProperty.addTag("t2", "tv2");
        secondProperty.addKey("k2", "kv2");
        insertPropertyCheck(secondProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", "*");
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("exactMatch", true);


        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void testExactFalseWildcart() throws Exception {
        final Property property = new Property("query-type16", "query-entity16");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        insertPropertyCheck(property);

        final Property secondProperty = new Property(null, "query-entity16-2");
        secondProperty.setType(property.getType());
        secondProperty.addTag("t2", "tv2");
        secondProperty.setKey(property.getKey());
        secondProperty.addKey("k2", "kv2");
        insertPropertyCheck(secondProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", "*");
        queryObj.put("key", property.getKey());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("exactMatch", false);


        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
            add(secondProperty);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }



    @Test
    public void testExactTrueWildcartKeyMatch() throws Exception {
        final Property property = new Property("query-type15", "query-entity15");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        insertPropertyCheck(property);

        final Property secondProperty = new Property(null, "query-entity15-2");
        secondProperty.setType(property.getType());
        secondProperty.addTag("t2", "tv2");
        secondProperty.addKey("k2", "kv2");
        insertPropertyCheck(secondProperty);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", "*");
        queryObj.put("key", property.getKey());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("exactMatch", true);


        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void testExactFalseDiffKey() throws Exception {
        final Property property = new Property("query-type14", "query-entity14");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.addKey("k2", "kv2");
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("key", new HashMap<String, String>(){{put("misskey", "misskey_value");}});
        queryObj.put("exactMatch", true);

        JSONAssert.assertEquals("[]", queryProperty(queryObj), false);
    }


    @Test
    public void testExactTrueDiffKey() throws Exception {
        final Property property = new Property("query-type13", "query-entity13");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.addKey("k2", "kv2");
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("key", new HashMap<String, String>(){{put("misskey", "misskey_value");}});
        queryObj.put("exactMatch", true);

        JSONAssert.assertEquals("[]", queryProperty(queryObj), false);
    }

    @Test
    public void testExactTruePartialMatch() throws Exception {
        final Property property = new Property("query-type12", "query-entity12");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.addKey("k2", "kv2");
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("key", new HashMap<String, String>(){{put("k1", "kv1");}});
        queryObj.put("exactMatch", true);

        JSONAssert.assertEquals("[]", queryProperty(queryObj), false);
    }



    @Test
    public void testExactFalseFullMatch() throws Exception {
        final Property property = new Property("query-type11", "query-entity11");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("key", property.getKey());
        queryObj.put("exactMatch", false);

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void testExactTrueFullMatch() throws Exception {
        final Property property = new Property("query-type10", "query-entity10");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", Util.getMinDate());
        queryObj.put("endDate", "now");
        queryObj.put("key", property.getKey());
        queryObj.put("exactMatch", true);

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void testEntities() throws Exception {
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
    public void testLastDefault() throws Exception {
        final Property property = new Property("query-type8", "query-entity8");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPreviousDay());
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
    public void testLastFalse() throws Exception {
        final Property property = new Property("query-type7", "query-entity7");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPreviousDay());
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
    public void testLastTrue() throws Exception {
        final Property property = new Property("query-type6", "query-entity6");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getPreviousDay());
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
    public void testStartPastIntervalGiveFuture() throws Exception {
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
    public void testStartEQDateInterval1MS() throws Exception {
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
    public void testStartPastEndFuture() throws Exception {
        final Property property = new Property("query-type3", "query-entity3");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("key", property.getKey());
        queryObj.put("startDate", Util.ISOFormat(Util.getPreviousDay()));
        queryObj.put("endDate", Util.ISOFormat(Util.getNextDay()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }


    @Test
    public void testTypeEntityStartEndExactDefault() throws Exception {
        final Property property = new Property("query-type2", "query-entity2");
        property.addTag("t1", "tv1");
        property.addKey("k1", "kv1");
        property.setDate(Util.getCurrentDate());
        insertPropertyCheck(property);

        Map<String, Object> queryObj = new HashMap<>();
        queryObj.put("type", property.getType());
        queryObj.put("entity", property.getEntity());
        queryObj.put("startDate", property.getDate());
        queryObj.put("endDate", Util.ISOFormat(Util.getNextDay()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void testTypeEntityStartEndExactFalse() throws Exception {
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
        queryObj.put("endDate", Util.ISOFormat(Util.getNextDay()));

        String expected = jacksonMapper.writeValueAsString(new ArrayList<Property>() {{
            add(property);
        }});

        JSONAssert.assertEquals(expected, queryProperty(queryObj), false);
    }

    @Test
    public void testTypeEntityStartEndPartkey() throws Exception {
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
    public void testTypeEntity() throws IOException {
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
    public void testType() throws IOException {
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
    public void testTypeStartInFuture() throws IOException {
        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", "testtype");
                put("startDate", "9999-06-01T12:04:59.191Z");
            }});
        }};

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, request.toJSONString());
        assertEquals(400, response.getCode());
        assertEquals("{\"error\":\"IllegalArgumentException: Missing parameters. One of the following combinations is required: interval, interval + startTime/startDate, interval + endTime/endDate, startTime/startDate + endTime/endDate\"}", response.getBody());
    }


    @Test
    public void testTypeStart() throws IOException {
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
    public void testTypeStartEnd() throws IOException {
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
