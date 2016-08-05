package com.axibase.tsd.api.method.entityGroup;

import com.axibase.tsd.api.model.entityGroup.EntityGroup;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.*;

import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author Dmitry Korchagin.
 */
public class EntityGroupEntitiesAddTest extends EntityGroupMethod {

    /**
     * #1278
     */
    @Test
    public void testNameContainsWhitespace() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodeaddentities entitygroup1");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    /**
     * #1278
     */
    @Test
    public void testNameContainsSlash() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodeaddentities/entitygroup2");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    /**
     * #1278
     */
    @Test
    public void testNameContainsCyrillic() throws Exception {
        EntityGroup entityGroup = new EntityGroup("urlencodeaddentitiesйёentitygroup3");
        assertUrlEncodePathHandledCorrectly(entityGroup);

    }

    public void assertUrlEncodePathHandledCorrectly(final EntityGroup entityGroup) throws Exception {
        createOrReplaceEntityGroupCheck(entityGroup);

        List<String> entitiesList = Arrays.asList("entity1", "entity2");
        List<Map> expectedResponse = new ArrayList<>();
        for (String s : entitiesList) {
            Map<String, Object> element = new HashMap<>();
            element.put("name", s);
            element.put("enabled", true);
            expectedResponse.add(element);
        }

        Response response = addEntities(entityGroup.getName(), entitiesList);
        assertEquals("Fail to execute addEntities query", OK.getStatusCode(), response.getStatus());

        String expected = jacksonMapper.writeValueAsString(expectedResponse);
        response = getEntities(entityGroup.getName());
        if (OK.getStatusCode() != response.getStatus()) {
            throw new IllegalArgumentException("Fail to execute getEntities query");
        }
        assertTrue("Entities in response do not match to added entities", compareJsonString(expected, formatToJsonString(response)));
    }
}