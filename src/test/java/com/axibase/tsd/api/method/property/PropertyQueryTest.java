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
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author Dmitry Korchagin.
 */

@SuppressWarnings("unchecked")
public class PropertyQueryTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
        setUpRegistry();
    }

    @Test
    public void test_TypeEntityStartEnd_LastDEFAULT_bothFounded() throws IOException {
        final Property propertyFirst = new PropertyBuilder().buildRandom();
        propertyFirst.setType(registry.registerType("query-last-type8"));
        propertyFirst.setEntity(registry.registerEntity("query-last-entity8"));
        propertyFirst.setDate(Util.format(Util.getPastDate()));
        if (!insertProperty(propertyFirst) || !propertyExist(propertyFirst)) {
            fail("Fail to insert propertyFirst");
        }
        logger.info("propertyFirst inserted");

        final Property propertyLast = new PropertyBuilder().buildRandom();
        propertyLast.setType(propertyFirst.getType());
        propertyLast.setEntity(propertyFirst.getEntity());
        propertyLast.setDate(Util.format(Util.getCurrentDate()));
        if (!insertProperty(propertyLast) || !propertyExist(propertyLast)) {
            fail("Fail to insert propertyLast");
        }
        logger.info("propertyLast inserted");

        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", propertyFirst.getEntity());
                    put("type", propertyFirst.getType());
                    put("startDate", propertyFirst.getDate());
                    put("interval", new JSONObject() {{
                        put("count", 2);
                        put("unit", "DAY");
                    }});
                }});
            }};
            assertTrue("Last property should be founded", propertiesExist(request, buildJsonArray(propertyFirst, propertyLast)));
        }
    }

    @Test
    public void test_TypeEntityStartEnd_LastFALSE_bothFounded() throws IOException {
        final Property propertyFirst = new PropertyBuilder().buildRandom();
        propertyFirst.setType(registry.registerType("query-last-type7"));
        propertyFirst.setEntity(registry.registerEntity("query-last-entity7"));
        propertyFirst.setDate(Util.format(Util.getPastDate()));
        if (!insertProperty(propertyFirst) || !propertyExist(propertyFirst)) {
            fail("Fail to insert propertyFirst");
        }
        logger.info("propertyFirst inserted");

        final Property propertyLast = new PropertyBuilder().buildRandom();
        propertyLast.setType(propertyFirst.getType());
        propertyLast.setEntity(propertyFirst.getEntity());
        propertyLast.setDate(Util.format(Util.getCurrentDate()));
        if (!insertProperty(propertyLast) || !propertyExist(propertyLast)) {
            fail("Fail to insert propertyLast");
        }
        logger.info("propertyLast inserted");

        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", propertyFirst.getEntity());
                    put("type", propertyFirst.getType());
                    put("startDate", propertyFirst.getDate());
                    put("interval", new JSONObject() {{
                        put("count", 2);
                        put("unit", "DAY");
                    }});
                    put("last", false);
                }});
            }};
            assertTrue("Last property should be founded", propertiesExist(request, buildJsonArray(propertyFirst, propertyLast)));
        }
    }

    @Test
    public void test_TypeEntityStartEnd_LastTRUE_propertyLastFounded() throws IOException {
        final Property propertyFirst = new PropertyBuilder().buildRandom();
        propertyFirst.setType(registry.registerType("query-last-type6"));
        propertyFirst.setEntity(registry.registerEntity("query-last-entity6"));
        propertyFirst.setDate(Util.format(Util.getPastDate()));
        if (!insertProperty(propertyFirst) || !propertyExist(propertyFirst)) {
            fail("Fail to insert propertyFirst");
        }
        logger.info("propertyFirst inserted");

        final Property propertyLast = new PropertyBuilder().buildRandom();
        propertyLast.setType(propertyFirst.getType());
        propertyLast.setEntity(propertyFirst.getEntity());
        propertyLast.setDate(Util.format(Util.getCurrentDate()));
        if (!insertProperty(propertyLast) || !propertyExist(propertyLast)) {
            fail("Fail to insert propertyLast");
        }
        logger.info("propertyLast inserted");

        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", propertyFirst.getEntity());
                    put("type", propertyFirst.getType());
                    put("startDate", propertyFirst.getDate());
                    put("interval", new JSONObject() {{
                        put("count", 2);
                        put("unit", "DAY");
                    }});
                    put("last", true);
                }});
            }};
            assertFalse("First property should not be founded", propertiesExist(request, buildJsonArray(propertyFirst)));

            assertTrue("Last property should be founded", propertiesExist(request, buildJsonArray(propertyLast)));
        }
    }

    @Test
    public void test_TypeEntity_StartPast_IntervalGiveFuture_propertyFounded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("query-type5"));
        property.setEntity(registry.registerEntity("query-entity5"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", property.getEntity());
                    put("type", property.getType());
                    put("startDate", Util.format(Util.getPastDate()));
                    put("interval", new JSONObject() {{
                        put("count", 2);
                        put("unit", "DAY");
                    }});
                }});
            }};
            assertTrue("Can not get property by specified request", propertiesExist(request, buildJsonArray(property)));
        }
    }


    @Test
    public void test_TypeEntity_StartEQDate_Interval1MS_propertyFinded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("query-type4"));
        property.setEntity(registry.registerEntity("query-entity4"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", property.getEntity());
                    put("type", property.getType());
                    put("startDate", property.getDate());
                    put("interval", new JSONObject() {{
                        put("count", 1);
                        put("unit", "MILLISECOND");
                    }});
                }});
            }};
            assertTrue("Can not get property by specified request", propertiesExist(request, buildJsonArray(property)));
        }
    }


    @Test
    public void test_TypeEntity_StartPast_EndFuture_propertyFounded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("query-type3"));
        property.setEntity(registry.registerEntity("query-entity3"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request;

        {
            request = new JSONArray() {{
                add(new JSONObject() {{
                    put("entity", property.getEntity());
                    put("type", property.getType());
                    put("startDate", Util.format(Util.getPastDate()));
                    put("endDate", Util.format(Util.getFutureDate()));
                }});
            }};
            assertTrue("Can not get property by specified request", propertiesExist(request, buildJsonArray(property)));
        }
    }


    @Test
    public void test_TypeEntityStartEnd_propertyFounded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("query-type2"));
        property.setEntity(registry.registerEntity("query-entity2"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request;

        request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("startDate", property.getDate());
                put("endDate", Util.format(Util.getFutureDate()));
            }});
        }};
        assertTrue("Can not get property by specified request", propertiesExist(request, buildJsonArray(property)));

    }

    @Test
    public void test_TypeEntityStartEnd_ExactFalse_propertyFounded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("query-type1"));
        property.setEntity(registry.registerEntity("query-entity1"));
        if (!insertProperty(property) || !propertyExist(property)) {
            fail("Fail to insert property");
        }
        logger.info("Property inserted");
        JSONArray request;

        request = new JSONArray() {{
            add(new JSONObject() {{
                put("entity", property.getEntity());
                put("type", property.getType());
                put("exactMatch", false);
                put("startDate", property.getDate());
                put("endDate", Util.format(Util.getFutureDate()));
            }});
        }};
        assertTrue("Can not get property by specified request", propertiesExist(request, buildJsonArray(property)));

    }

    @Test
    public void test_Example_TypeEntityStartEnd_Partkey_propertyFounded() throws IOException {
        final Property property = new PropertyBuilder().buildRandom();
        property.setType(registry.registerType("disk"));
        property.setEntity(registry.registerEntity("nurswgvml007"));
        property.setKey(new HashMap<String, String>(){{
                put(registry.registerKeyName("file_system"), registry.registerKeyValue("/"));
                put(registry.registerKeyName("mount_point"), registry.registerKeyValue("/sda1"));
        }});
        property.setTags(new HashMap<String, String>(){{
            put(registry.registerTagName("fs_type"), registry.registerTagValue("ext4"));
        }});
        property.setDate("2016-05-25T04:00:00.000Z");
        assertTrue(insertProperty(property));
        assertTrue("Fail to insert property", propertyExist(property));

        JSONArray request = new JSONArray() {{
            add(new JSONObject() {{
                put("type", property.getType());
                put("entity", property.getEntity());
                put("key", new JSONObject(){{
                    put("file_system", "/");
                }});
                put("startDate", "2016-05-25T04:00:00Z");
                put("endDate", "2016-05-25T05:00:00Z");
            }});
        }};

        assertTrue(propertiesExist(request, buildJsonArray(property)));
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
