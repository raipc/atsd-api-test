package com.axibase.tsd.api.method.property.transport.tcp;

import com.axibase.tsd.api.method.checks.PropertyCheck;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.command.PlainCommand;
import com.axibase.tsd.api.model.command.PropertyCommand;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.transport.tcp.TCPSender;
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
    public void emptyTagFailTest() throws Exception {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.<String, String>builder()
                .put("t1", "\r")
                .build();
        property.setTags(tagMap);
        PlainCommand command = new PropertyCommand(property);
        String result = TCPSender.send(command, true);
        assertTrue(command.compose() + " had to fail, but did not.", result.startsWith("Empty tag values in command"));
    }

    @Issue("6234")
    @Description("Tests that if one tag is empty and one is not, property is inserted and tag with empty value is deleted.")
    @Test
    public void emptyAndNonEmptyTagTest() throws Exception {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.<String, String>builder()
                .put("t1", "v1")
                .put("t2", "v2")
                .build();
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand nonEmptyCommand = new PropertyCommand(property);
        TCPSender.send( nonEmptyCommand, true);

        Map<String, String> emptyTagMap = ImmutableMap.<String, String>builder()
                .put("t1","v1-new")
                .put("t2", "\r")
                .build();
        property.setTags(emptyTagMap);
        PlainCommand emptyTagCommand = new PropertyCommand(property);
        TCPSender.send(emptyTagCommand, true);

        property.setTags(ImmutableMap.<String, String>builder().put("t1", "v1-new").build());
        assertPropertyExisting(property);
    }

    @Issue("6234")
    @Description("Tests that if key is not empty and the only tg is empty, property is not inserted.")
    @Test
    public void keyAndEmptyTagTest() throws Exception {
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
        PlainCommand command = new PropertyCommand(property);
        String result = TCPSender.send(command, true);
        assertTrue(command.compose() + " had to fail, but did not.", result.startsWith("Empty tag values in command"));
    }
}