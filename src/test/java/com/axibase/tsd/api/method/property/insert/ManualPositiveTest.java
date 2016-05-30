package com.axibase.tsd.api.method.property.insert;


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
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Dmitry Korchagin.
 */

public class ManualPositiveTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Property property;

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }

    @Test
    public void propertyInsert_CorrectProperty_PropertyAppear() throws IOException {

        String insertRequest = "[\n" +
                "  {\n" +
                "    \"type\": \"disk\",\n" +
                "    \"entity\": \"nurswgvml007\",\n" +
                "    \"key\": {\n" +
                "      \"file_system\": \"/\",\n" +
                "      \"mount_point\": \"sda1\"\n" +
                "    },\n" +
                "    \"tags\": {\n" +
                "      \"fs_type\": \"ext4\"\n" +
                "    },\n" +
                "    \"date\": \"2016-05-25T04:15:00Z\"\n" +
                "  }\n" +
                "]";

        logger.debug("prepared insert request: {}", insertRequest);

        AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_INSERT, insertRequest);

        assertEquals("Fail to insert property", 200, response.getCode());

        String queryRequest = "[\n" +
                "    {\n" +
                "      \"type\": \"disk\",\n" +
                "      \"entity\": \"nurswgvml007\",\n" +
                "      \"key\": { \"file_system\": \"/\" },\n" +
                "      \"startDate\": \"2016-05-25T04:00:00Z\",\n" +
                "      \"endDate\":   \"2016-05-25T05:00:00Z\"\n" +
                "     }\n" +
                "]";
        logger.debug("prepared query request: {}", queryRequest);


        String expectedResponse = "[\n" +
                "   {\n" +
                "       \"date\": \"2016-05-25T04:15:00Z\",\n" +
                "       \"type\": \"disk\",\n" +
                "       \"entity\": \"nurswgvml007\",\n" +
                "       \"key\": {\n" +
                "           \"file_system\": \"/\",\n" +
                "           \"mount_point\": \"sda1\"\n" +
                "       },\n" +
                "       \"tags\": {\n" +
                "           \"fs_type\": \"ext4\"\n" +
                "       }\n" +
                "   }\n" +
                "]";
        logger.debug("prepared expected response: {}", expectedResponse);

        response = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, queryRequest);

        assertEquals("Fail to execute query request", 200, response.getCode());

        try {
            JSONArray responseObject = (JSONArray) new JSONParser().parse(response.getBody());
            JSONArray expectedResponseObject = (JSONArray) new JSONParser().parse(expectedResponse);
            assertEquals(expectedResponseObject, responseObject);
        } catch (ParseException e) {
            logger.error("Fail to parse response body or expected response. Response: {}, Expected response: {}", response.getBody(), expectedResponse);
            fail();
        }
    }
}
