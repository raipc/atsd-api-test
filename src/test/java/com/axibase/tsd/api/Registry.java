package com.axibase.tsd.api;

import com.axibase.tsd.api.model.propery.Property;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.fail;

/**
 * @author Dmitry Korchagin.
 */
public  class Registry {

    private Set<String> entitySet = new TreeSet<>();

    private Set<String> typeSet = new TreeSet<>();

    private Set<String> tagNameSet = new TreeSet<>();

    private Set<String> tagValueSet = new TreeSet<>();

    private static Registry instance = null;

    public static Registry getInstance() {
        if(instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    private Registry() {
        entitySet = new TreeSet<>();
        typeSet = new TreeSet<>();
        tagNameSet = new TreeSet<>();
        tagValueSet = new TreeSet<>();

    }

    public String registerEntity(String value) {
        if(entitySet.contains(value)) {
            throw new IllegalArgumentException("Entity already registred");
        }
        entitySet.add(value);
        return value;
    }

    public String registerType(String value) {
        if(typeSet.contains(value)) {
            throw new IllegalArgumentException("Type already registred");
        }
        typeSet.add(value);

        return value;

    }

    public String registerTagName(String value) {
        if(tagNameSet.contains(value)) {
            throw new IllegalArgumentException("TagName already registred");
        }
        tagNameSet.add(value);
        return value;
    }

    public String registerTagValue(String value) {
        if(tagValueSet.contains(value)) {
            throw new IllegalArgumentException("TagValue already registred");
        }
        tagValueSet.add(value);
        return value;
    }

    public String registerKeyName(String value) {
        return registerTagName(value);
    }

    public String registerKeyValue(String value) {
        return registerTagValue(value);
    }

}
