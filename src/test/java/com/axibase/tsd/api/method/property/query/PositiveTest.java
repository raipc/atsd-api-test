package com.axibase.tsd.api.method.property.query;


import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
public class PositiveTest extends PropertyMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DATASET_NAME = "positiveQuery.json";




    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
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
            logger.debug("insert: {}", insert);

            JSONArray query = (JSONArray) querySet.get(1);
            logger.debug("query: {}", query);

            JSONArray response = (JSONArray) querySet.get(2);
            logger.debug("response: {}", response);


            AtsdHttpResponse atsdResponse = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_INSERT, insert.toJSONString());
            assertEquals("Fail to insert property", 200, atsdResponse.getCode());

            atsdResponse = httpSender.send(HTTPMethod.POST, METHOD_PROPERTY_QUERY, query.toJSONString());
            assertEquals("Fail to execute query request", 200, atsdResponse.getCode());

            try {
                JSONArray atsdResponseData = (JSONArray) new JSONParser().parse(atsdResponse.getBody());
                assertTrue("ATSD Response does not contain all required objects", atsdResponseData.containsAll(response));
            } catch (ParseException e) {
                logger.error("Fail to parse response body. Response: {}", atsdResponse.getBody());
                fail();
            }

        }
    }
}