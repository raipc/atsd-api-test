package com.axibase.tsd.api.builder;

import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.model.propery.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * @author Dmitry Korchagin.
 */
public class PropertyBuilder implements Builder<Property> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final int ENTITY_LENGTH = 8;
    private final int TYPE_LENGTH = 8;
    private final int KEY_COUNT = 2;
    private final int KEY_NAME_LENGTH = 4;
    private final int KEY_VALUE_LENGTH = 4;
    private final int TAG_COUNT = 2;
    private final int TAG_NAME_LENGTH = 4;
    private final int TAG_VALUE_LENGTH = 4;



    public Property buildRandom() {
        Property property = new Property();
        property.setEntity(Util.ABNF.generateNAME(ENTITY_LENGTH).toLowerCase());
        property.setType(Util.ABNF.generateNAME(TYPE_LENGTH).toLowerCase());
        property.setTags(generateTag(TAG_COUNT));
        property.setKey(generateKey(KEY_COUNT));
        property.setDate(Util.format(Util.getCurrentDate()));
        return property;
    }

    private Map<String, String> generateTag(int tagCount) {
        Map<String, String> tags = new HashMap<>();
        for (int i = 0; i < tagCount; i++) {
            tags.put(Util.ABNF.generateNAME(TAG_NAME_LENGTH).toLowerCase(), Util.ABNF.generateTEXTVALUE(TAG_VALUE_LENGTH));
        }
        return tags;
    }


    private Map<String, String> generateKey(int keyCount) {
        Map<String, String> keys = new HashMap<>();
        for (int i = 0; i < keyCount; i++) {
            keys.put(Util.ABNF.generateNAME(KEY_NAME_LENGTH).toLowerCase(), Util.ABNF.generateTEXTVALUE(KEY_VALUE_LENGTH));
        }
        return keys;
    }




}
