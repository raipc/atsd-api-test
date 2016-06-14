package com.axibase.tsd.api.transport.http;

/**
 * @author Dmitry Korchagin.
 */
public enum ContentType {
    JSON("application/json"),
    CSV("text/csv");

    private String value;

    private ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
