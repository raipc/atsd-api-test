package com.axibase.tsd.api.method.property.command;

import com.axibase.tsd.api.method.checks.NotPassedCheck;
import com.axibase.tsd.api.method.checks.PropertyCheck;
import com.axibase.tsd.api.method.extended.CommandMethod;
import com.axibase.tsd.api.method.property.PropertyMethod;
import com.axibase.tsd.api.model.command.PlainCommand;
import com.axibase.tsd.api.model.command.PropertyCommand;
import com.axibase.tsd.api.model.property.Property;
import io.qameta.allure.Issue;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.axibase.tsd.api.method.property.PropertyTest.assertPropertyExisting;
import static com.axibase.tsd.api.util.TestUtil.getCurrentDate;

public class EmptyTagTest extends PropertyMethod {


    @Issue("6234")
    @Test
    public void emptyTagFailTest() {
        Property property = new Property("property-command-emptytag-test-t1", "property-command-emptytag-test-e1");
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "\r");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        CommandMethod.send(command);
        tagMap.remove("t1");
        property.setTags(tagMap);
        NotPassedCheck.assertNotPassed(property.toString() + " was inserted", new PropertyCheck(property));
    }

    @Issue("6234")
    @Test
    public void emptyAndNonEmptyTagTest() {
        Property property = new Property("property-command-emptytag-test-t2", "property-command-emptytag-test-e2");
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "v1");
        tagMap.put("t2", "v2");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand nonEmptyCommand = new PropertyCommand(property);
        CommandMethod.send(nonEmptyCommand);

        Map<String, String> emptyTagMap = new HashMap<>();
        emptyTagMap.put("t1", "v1-new");
        emptyTagMap.put("t2", "\r");
        property.setTags(emptyTagMap);
        PlainCommand emptyTagCommand = new PropertyCommand(property);
        CommandMethod.send(emptyTagCommand);

        emptyTagMap.remove("t2");
        property.setTags(emptyTagMap);
        assertPropertyExisting(property);
    }

    @Issue("6234")
    @Test
    public void keyAndEmptyTagTest() {
        Property property = new Property("property-command-emptytag-test-t3", "property-command-emptytag-test-e3");
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("k1", "vk1");
        property.setKey(keyMap);
        Map<String, String> tagMap = new HashMap<>();
        tagMap.put("t1", "\r");
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        CommandMethod.send(command);
        tagMap.remove("t1");
        property.setTags(tagMap);
        NotPassedCheck.assertNotPassed(property.toString() + " was inserted", new PropertyCheck(property));
    }
}
