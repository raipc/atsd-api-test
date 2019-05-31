package com.axibase.tsd.api.method.property.transport.tcp;

import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.command.PlainCommand;
import com.axibase.tsd.api.model.command.PropertyCommand;
import com.axibase.tsd.api.model.property.Property;
import com.axibase.tsd.api.transport.tcp.TCPSender;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;


import java.util.HashMap;
import java.util.Map;

import static com.axibase.tsd.api.method.property.PropertyTest.assertPropertyExisting;
import static com.axibase.tsd.api.util.TestUtil.getCurrentDate;
import static org.testng.AssertJUnit.assertTrue;

public class EmptyTagTest extends PropertyMethod {


    @Issue("6234")
    @Test
    public void emptyTagFailTest() throws Exception {
        Property property = new Property("property-tcp-emptytag-test-t1", "property-tcp-emptytag-test-e1");
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "\r");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        String result = TCPSender.send(command, true);
        tagMap.remove("t1");
        property.setTags(tagMap);
        assertTrue(command.compose() + " had to fail, but did not.", result.startsWith("Empty tag values in command"));
    }

    @Issue("6234")
    @Test
    public void emptyAndNonEmptyTagTest() throws Exception {
        Property property = new Property("property-tcp-emptytag-test-t2", "property-tcp-emptytag-test-e2");
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "v1");
        tagMap.put("t2", "v2");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand nonEmptyCommand = new PropertyCommand(property);
        TCPSender.send(nonEmptyCommand, true);
        //CommandMethod.send(nonEmptyCommand);

        Map<String, String> emptyTagMap = new HashMap<>();
        emptyTagMap.put("t1", "v1-new");
        emptyTagMap.put("t2", "\r");
        property.setTags(emptyTagMap);
        PlainCommand emptyTagCommand = new PropertyCommand(property);
        TCPSender.send(emptyTagCommand, true);

        emptyTagMap.remove("t2");
        property.setTags(emptyTagMap);
        assertPropertyExisting(property);
    }

    @Issue("6234")
    @Test
    public void keyAndEmptyTagTest() throws Exception {
        Property property = new Property("property-tcp-emptytag-test-t3", "property-tcp-emptytag-test-e3");
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("k1", "vk1");
        property.setKey(keyMap);
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "\r");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        String result = TCPSender.send(command, true);
        tagMap.remove("t1");
        property.setTags(tagMap);
        assertTrue(command.compose() + " had to fail, but did not.", result.startsWith("Empty tag values in command"));
    }
}