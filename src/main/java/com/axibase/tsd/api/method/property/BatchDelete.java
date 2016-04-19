package com.axibase.tsd.api.method.property;


import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author Dmitry Korchagin.
 */

public class BatchDelete extends Method {
    private static final String ATSD_METHOD = "/properties";
    private static final Logger logger = LoggerFactory.getLogger(BatchDelete.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }


    @Test
    public void batchPropertyDelete_CorrectPropertyDelete_PropertyDisappear() throws IOException {
        final String type = Util.buildVariablePrefix() + "type";
        final String entity = Util.buildVariablePrefix() + "entity";
        final Long timestamp = System.currentTimeMillis();

        JsonArray insertPropertyRequest;
        JsonArray expectedResponse;
        JsonObject request;
        JsonArray deletePropertyRequest;

        {
            insertPropertyRequest = Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                                    .add("type", type)
                                    .add("entity", entity)
                                    .add("key", Json.createObjectBuilder()
                                                    .add("key1", "keyval1")
                                    ).add("tags", Json.createObjectBuilder()
                                            .add("tag1", "tagval1"))
                                    .add("timestamp", timestamp)
                    )
                    .build();

            request = Json.createObjectBuilder()
                    .add("queries", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                            .add("type", type)
                                            .add("entity", entity)
                                            .add("key", Json.createObjectBuilder()
                                                            .add("key1", "keyval1")
                                            )
                            ))
                    .build();

            expectedResponse = Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                                    .add("type", type)
                                    .add("entity", entity)
                                    .add("key", Json.createObjectBuilder()
                                                    .add("key1", "keyval1")
                                    ).add("tags", Json.createObjectBuilder()
                                            .add("tag1", "tagval1"))
                                    .add("timestamp", timestamp)
                    )
                    .build();

            deletePropertyRequest = Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                                    .add("action", "delete")
                                    .add("properties", Json.createArrayBuilder()
                                                    .add(Json.createObjectBuilder()
                                                                    .add("type", type)
                                                                    .add("entity", entity)
                                                                    .add("key", Json.createObjectBuilder()
                                                                                    .add("key1", "keyval1")
                                                                    )
                                                    )
                                    )
                    )
                    .build();
        }


        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, insertPropertyRequest.toString());
            assertEquals(200, response.getCode());
        }

        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, request.toString());
            assertEquals(200, response.getCode());
            assertEquals(expectedResponse, Json.createReader(new StringReader(response.getBody())).readObject());
        }

        {
            AtsdHttpResponse response = httpSender.send(HTTPMethod.PATH, ATSD_METHOD, deletePropertyRequest.toString());
            assertEquals(200, response.getCode());

            response = httpSender.send(HTTPMethod.POST, ATSD_METHOD, request.toString());
            assertEquals(200, response.getCode());
            assertEquals(Json.createArrayBuilder().build(), Json.createReader(new StringReader(response.getBody())).readObject());
        }

    }

}
