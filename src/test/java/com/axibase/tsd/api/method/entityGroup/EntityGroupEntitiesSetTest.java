package com.axibase.tsd.api.method.entityGroup;

import com.axibase.tsd.api.model.entityGroup.EntityGroup;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.*;

import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Dmitry Korchagin.
 */
public class EntityGroupEntitiesSetTest extends EntityGroupMethod {

    /**
     * #1278
     */
    @Test
    public void testNameContainsWhitespace() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodesetentities entitygroup1");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    /**
     * #1278
     */
    @Test
    public void testNameContainsSlash() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodesetentities/entitygroup2");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    /**
     * #1278
     */
    @Test
    public void testNameContainsCyrillic() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodesetentitiesйёentitygroup3");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    public void assertUrlEncodePathHandledCorrectly(final EntityGroup entityGroup) throws Exception {
        createOrReplaceEntityGroupCheck(entityGroup);

        List<String> entitiesDefault = Arrays.asList("entity1", "entity2");
        List<String> entitiesToSet = Arrays.asList("entity3", "entity4");
        deleteEntities(entityGroup.getName(), entitiesToSet);

        List<Map> expectedResponse = new ArrayList<>();
        for (String s : entitiesDefault) {
            Map<String, Object> element = new HashMap<>();
            element.put("name", s);
            element.put("enabled", true);
            expectedResponse.add(element);
        }

        Response response = addEntities(entityGroup.getName(), entitiesDefault);
        if (OK.getStatusCode() != response.getStatus()) {
            throw new IllegalStateException("Fail to execute addEntities query");
        }

        String expected = jacksonMapper.writeValueAsString(expectedResponse);
        response = getEntities(entityGroup.getName());
        if (OK.getStatusCode() != response.getStatus()) {
            throw new IllegalArgumentException("Fail to execute getEntities query");
        }
        if (!compareJsonString(expected, formatToJsonString(response))) {
            throw new IllegalStateException("Fail to get added entities");
        }

        expectedResponse = new ArrayList<>();
        for (String s : entitiesToSet) {
            Map<String, Object> element = new HashMap<>();
            element.put("name", s);
            element.put("enabled", true);
            expectedResponse.add(element);
        }

        response = setEntities(entityGroup.getName(), entitiesToSet);
        assertEquals("Fail to execute deleteEntities query", OK.getStatusCode(), response.getStatus());

        expected = jacksonMapper.writeValueAsString(expectedResponse);
        response = getEntities(entityGroup.getName());
        if (OK.getStatusCode() != response.getStatus()) {
            throw new IllegalArgumentException("Fail to execute getEntities query");
        }
        if (!compareJsonString(expected, formatToJsonString(response))) {
            throw new IllegalStateException("Fail to get added entities");
        }
    }
}
