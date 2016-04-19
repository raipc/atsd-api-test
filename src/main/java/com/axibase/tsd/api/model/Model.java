package com.axibase.tsd.api.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
public abstract class Model {

    public String serialize() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public <T> T deserialize(String json, Class<T> clazz) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(json, clazz);
    }
}
