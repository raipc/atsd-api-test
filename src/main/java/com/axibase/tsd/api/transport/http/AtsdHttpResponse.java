package com.axibase.tsd.api.transport.http;

import java.util.Map;

/**
 * @author Dmitry Korchagin.
 */
public class AtsdHttpResponse {
    private int code;
    private Map<String, String> headers;
    private String body;

    AtsdHttpResponse(int code, Map<String, String> headers, String body) {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "AtsdResponse{" +
                "code=" + code +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
