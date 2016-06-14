package com.axibase.tsd.api.transport.http;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
interface HTTPClient {


    abstract AtsdHttpResponse get(String atsdMethod, ContentType contentType) throws IOException;

    abstract AtsdHttpResponse post(String atsdMethod, String body, ContentType contentType) throws IOException;

    abstract AtsdHttpResponse put(String atsdMethod, String body, ContentType contentType) throws IOException;

    abstract AtsdHttpResponse patch(String atsdMethod, String body, ContentType contentType) throws IOException;

    abstract AtsdHttpResponse delete(String atsdMethod) throws IOException;
}
