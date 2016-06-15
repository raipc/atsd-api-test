package com.axibase.tsd.api.method.entity;

import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.entity.Entity;
import com.axibase.tsd.api.transport.http.AtsdHttpResponse;
import com.axibase.tsd.api.transport.http.HTTPMethod;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Korchagin.
 */
public class EntityMethod extends Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    static final String METHOD_ENTITIES = "/entities/";

    void createOrUpdate(final Entity entity) throws IOException {
        final String path = METHOD_ENTITIES + entity.getName();

        AtsdHttpResponse response = httpSender.send(HTTPMethod.PUT, path, (new JSONObject(entity.getTags()).toJSONString()));
        if (response.getCode() != 200) {
            throw new IOException("Fail to insert entity");
        }
        if (!entityExist(entity)) {
            throw new IOException("Fail to check inserted property");
        }
    }


    Boolean entityExist(final Entity entity) throws IOException {
        return entityExist(entity, true);
    }

    Boolean entityExist(final Entity entity, Boolean allowExtraFields) throws IOException {
        String propertyJson = jacksonMapper.writeValueAsString(new ArrayList<Entity>() {{
            add(entity);
        }});

        AtsdHttpResponse response = httpSender.sendGet(METHOD_ENTITIES + entity.getName());
        assertEquals(200, response.getCode());
        logger.debug("check: {}\nresponse: {}", propertyJson, response.getBody());

        try {
            JSONAssert.assertEquals(propertyJson, response.getBody(), allowExtraFields ? JSONCompareMode.LENIENT : JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException e) {
            throw new IOException("Can not deserialize response");
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }
}
