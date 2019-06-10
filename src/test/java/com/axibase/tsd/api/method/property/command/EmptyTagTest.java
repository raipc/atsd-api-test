package com.axibase.tsd.api.method.property.command;

import com.axibase.tsd.api.method.extended.CommandMethod;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.command.PlainCommand;
import com.axibase.tsd.api.model.command.PropertyCommand;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.model.extended.CommandSendingResult;
import com.axibase.tsd.api.util.Mocks;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.axibase.tsd.api.method.property.PropertyTest.assertPropertyExisting;
import static com.axibase.tsd.api.util.TestUtil.getCurrentDate;
import static org.testng.AssertJUnit.assertTrue;

public class EmptyTagTest extends PropertyMethod {

    @Issue("6234")
    @Description("Tests that if the only tag in insertion command is empty, it fails.")
    @Test
    public void emptyTagFailTest() {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.<String, String>builder()
                .put("t1", "\r")
                .build();
        property.setTags(tagMap);
        PlainCommand command = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(command);
        assertTrue("The only command had to fail. Actual response: " + result.toString(), result.getFail() == 1);
        assertTrue("Total commands count is not 1: " + result.toString(), result.getTotal() == 1);
    }

    @Issue("6234")
    @Description("Tests that if one tag is empty and one is not, property is inserted and tag with empty value is deleted.")
    @Test
    public void emptyAndNonEmptyTagTest() {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.<String, String>builder()
                .put("t1", "v1")
                .put("t2", "v2")
                .build();
        property.setTags(tagMap);
        PlainCommand nonEmptyCommand = new PropertyCommand(property);
        CommandMethod.send(nonEmptyCommand);

        Map<String, String> emptyTagMap = ImmutableMap.<String, String>builder()
                .put("t1","v1-new")
                .put("t2", "\r")
                .build();
        property.setTags(emptyTagMap);
        PlainCommand emptyTagCommand = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(emptyTagCommand);
        assertTrue("The only command had to succeed. Actual response: " + result.toString(), result.getSuccess() == 1);
        assertTrue("Total commands count is not 1: " + result.toString(), result.getTotal() == 1);

        property.setTags(ImmutableMap.<String, String>builder().put("t1", "v1-new").build());
        assertPropertyExisting(property);
    }

    @Issue("6234")
    @Description("Tests that if key is not empty and the only tg is empty, property is not inserted.")
    @Test
    public void keyAndEmptyTagTest() {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("k1", "vk1");
        property.setKey(keyMap);
        Map<String, String> tagMap = ImmutableMap.<String, String>builder()
                .put("t1", "\r")
                .build();
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(command);
        assertTrue("The only command had to fail. Actual response: " + result.toString(), result.getFail() == 1);
        assertTrue("Total commands count is not 1: " + result.toString(), result.getTotal() == 1);
    }
}
