package com.axibase.tsd.api.model.series;

import com.axibase.tsd.api.model.Model;

public class Entity extends Model {
    private String name;

    public Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
