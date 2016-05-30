package com.axibase.tsd.api.builder;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.propery.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Dmitry Korchagin.
 */
public class PropertyBuilder implements Builder<Property> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final int ENTITY_LENGTH = 15;
    private final int TYPE_LENGTH = 15;
    private final int KEY_COUNT = 3;
    private final int KEY_NAME_LENGTH = 15;
    private final int KEY_VALUE_LENGTH = 15;
    private final int TAG_COUNT = 3;
    private final int TAG_NAME_LENGTH = 15;
    private final int TAG_VALUE_LENGTH = 15;


    public Property build(Map fields) {
        Property property = new Property();
        return property;
    }

    public Property buildRandom() {
        Property property = new Property();
        property.setEntity(Util.ABNF.generateNAME(ENTITY_LENGTH));
        property.setType(Util.ABNF.generateNAME(TYPE_LENGTH));
        property.setTags(generateTag(TAG_COUNT));
        property.setKey(generateKey(KEY_COUNT));
        return property;
    }

    private Map<String, String> generateTag(int tagCount) {
        Map<String, String> tags = new HashMap<>();
        for(int i = 0; i < tagCount; i++) {
            tags.put(Util.ABNF.generateNAME(TAG_NAME_LENGTH), Util.ABNF.generateTEXTVALUE(TAG_VALUE_LENGTH));
        }
        return tags;
    }

    private Map<String, String> generateKey(int keyCount) {
        Map<String, String> tags = new HashMap<>();
        for(int i = 0; i < keyCount; i++) {
            tags.put(Util.ABNF.generateNAME(KEY_NAME_LENGTH), Util.ABNF.generateTEXTVALUE(KEY_VALUE_LENGTH));
        }
        return tags;
    }


}
