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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.axibase.tsd.api.method.property.PropertyTest.assertPropertyExisting;
import static com.axibase.tsd.api.util.TestUtil.getCurrentDate;
import static org.testng.AssertJUnit.*;

public class EmptyTagTest extends PropertyMethod {

    @DataProvider
    private Object[][] emptyValues() {
        return new String[][] {
                {""},
                {"\r"},
                {"\n"},
                {"\r\n"},
                {" "},
                {"  "},
                {" \n "},
                {" \n"},
                {"\n "}
        };
    }

    @Issue("6234")
    @Description("Tests that if the only tag in insertion command is empty, it fails.")
    @Test(
            dataProvider = "emptyValues"
    )
    public void emptyTagFailTest(String emptyValue) {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.of("t1", emptyValue);
        property.setTags(tagMap);
        PlainCommand command = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(command);
        assertEquals("The only command had to fail. Actual response: " + result.toString(), result.getFail(), Integer.valueOf(1));
        assertEquals("Total commands count is not 1: " + result.toString(), result.getTotal(), Integer.valueOf(1));
    }

    @Issue("6234")
    @Description("Tests that if one tag is empty and one is not, property is inserted and tag with empty value is deleted.")
    @Test(
            dataProvider = "emptyValues"
    )
    public void emptyAndNonEmptyTagTest(String emptyValue) {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> tagMap = ImmutableMap.of("t1", "v1", "t2", "v2");
        property.setTags(tagMap);
        PlainCommand nonEmptyCommand = new PropertyCommand(property);
        CommandMethod.send(nonEmptyCommand);

        Map<String, String> emptyTagMap = ImmutableMap.of("t1", "v1-new", "t2", emptyValue);
        property.setTags(emptyTagMap);
        PlainCommand emptyTagCommand = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(emptyTagCommand);
        assertEquals("The only command had to succeed. Actual response: " + result.toString(), result.getSuccess(), Integer.valueOf( 1));
        assertEquals("Total commands count is not 1: " + result.toString(), result.getTotal(), Integer.valueOf( 1));

        property.setTags(ImmutableMap.of("t1", "v1-new"));
        assertPropertyExisting(property);
    }

    @Issue("6234")
    @Description("Tests that if key is not empty and the only tg is empty, property is not inserted.")
    @Test(
            dataProvider = "emptyValues"
    )
    public void keyAndEmptyTagTest(String emptyValue) {
        String propertyType = Mocks.propertyType();
        String entityName = Mocks.entity();
        Property property = new Property(propertyType, entityName);
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("k1", "vk1");
        property.setKey(keyMap);
        Map<String, String> tagMap = ImmutableMap.of("t1", emptyValue);
        property.setTags(tagMap);
        property.setDate(getCurrentDate());
        PlainCommand command = new PropertyCommand(property);
        CommandSendingResult result = CommandMethod.send(command);
        assertEquals("The only command had to fail. Actual response: " + result.toString(), result.getFail(), Integer.valueOf(1));
        assertEquals("Total commands count is not 1: " + result.toString(), result.getTotal(), Integer.valueOf(1));
    }
}
