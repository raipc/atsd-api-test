package com.axibase.tsd.api.method.property;

import com.axibase.tsd.api.transport.http.AtsdResponse;
import com.axibase.tsd.api.method.Method;
import com.axibase.tsd.api.model.Model;
import com.axibase.tsd.api.model.propery.Property;
import com.axibase.tsd.api.model.propery.PropertyDelete;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class BatchDelete extends Method {
    private static final String ATSD_METHOD="/properties";
    private static final Logger logger = LoggerFactory.getLogger(BatchDelete.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        prepareRequestSender();
    }





    @Test
    public void batchPropertyDelete_CorrectPropertyDelete_PropertyDisappear() throws IOException {
        String type = buildVariablePrefix() + "type";
        String entity = buildVariablePrefix() + "entity";
        Map<String, String> key = new HashMap<String, String>();
        key.put("key1", "keyval1");
        Map<String, String> tags = new HashMap<String, String>();
        tags.put("tag1", "tagval1");
        final Property property = new Property(type, entity, key, tags, null);
        PropertyDelete propertyDelete = new PropertyDelete(type, entity, key, 0L);

        RequestStructure requestStructure = new RequestStructure(Arrays.asList((Model)propertyDelete));

        AtsdResponse response = requestSender.patch(ATSD_METHOD, serialize(Arrays.asList(requestStructure)));
        logger.debug("AtsdReponse: {}", response);
        assertEquals(200, response.getCode());
        assertEquals("", response.getBody());




    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    class RequestStructure {
        private final String action="delete";
        private List<Model> properties;

        public RequestStructure(List<Model> properties) {
            this.properties = properties;
        }

        public String getAction() {
            return action;
        }

        public List<Model> getProperties() {
            return properties;
        }
    }
}
