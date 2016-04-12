package com.axibase.tsd.api.method;

import com.axibase.tsd.api.Config;
import com.axibase.tsd.api.transport.http.HttpClientPure;
import com.axibase.tsd.api.transport.http.RequestSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
public abstract class Method {
    protected static RequestSender requestSender;

    protected static void prepareRequestSender() {
        Config config = Config.getInstance();
        HttpClientPure driver = new HttpClientPure(config.getProtocol(), config.getServerName(), config.getServerPort(), config.getLogin(), config.getPassword());
        requestSender = new RequestSender(driver, config.getDataPath(), config.getMetadataPath());
    }

    protected static String buildVariablePrefix() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < methodName.length(); i++) {
            Character ch = methodName.charAt(i);
            if (Character.isUpperCase(ch)) {
                prefix.append("-");
            }
            prefix.append(Character.toLowerCase(ch));
        }
        prefix.append("-");
        return prefix.toString();
    }

    public String serialize(Object object) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public <T> T deserialize(String json, Class<T> clazz) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(json, clazz);
    }

}
