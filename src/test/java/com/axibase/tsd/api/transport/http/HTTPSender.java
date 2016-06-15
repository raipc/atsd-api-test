package com.axibase.tsd.api.transport.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
public class HTTPSender {
    private static final Logger logger = LoggerFactory.getLogger(HTTPSender.class);

    private final HTTPClient driver;
    private final String dataPath;
    private final String metaDataPath;
    private final ContentType DEFAULT_CONTENT_TYPE = ContentType.JSON;

    public HTTPSender(HTTPClient driver, String dataPath, String metaDataPath) {
        this.driver = driver;
        this.dataPath = dataPath;
        this.metaDataPath = metaDataPath;
    }

    public AtsdHttpResponse send(HTTPMethod method, String uri, String body, ContentType contentType) throws IOException {
        logger.debug("> METHOD: {}", method);
        AtsdHttpResponse atsdHttpResponse;
        switch (method) {
            case GET:
                atsdHttpResponse = get(uri, contentType);
                break;
            case POST:
                atsdHttpResponse = post(uri, body, contentType);
                break;
            case PUT:
                atsdHttpResponse = put(uri, body, contentType);
                break;
            case PATH:
                atsdHttpResponse = patch(uri, body, contentType);
                break;
            case DELETE:
                atsdHttpResponse = delete(uri);
                break;
            default:
                atsdHttpResponse = null;
        }
        return atsdHttpResponse;
    }

    public AtsdHttpResponse sendGet(String uri) throws IOException {
        return send(HTTPMethod.GET, uri, null);
    }

    public AtsdHttpResponse send(HTTPMethod method, String uri, String body) throws IOException {
        logger.debug("> METHOD: {}", method);
        AtsdHttpResponse atsdHttpResponse;
        switch (method) {
            case GET:
                atsdHttpResponse = get(uri, DEFAULT_CONTENT_TYPE);
                break;
            case POST:
                atsdHttpResponse = post(uri, body, DEFAULT_CONTENT_TYPE);
                break;
            case PUT:
                atsdHttpResponse = put(uri, body, DEFAULT_CONTENT_TYPE);
                break;
            case PATH:
                atsdHttpResponse = patch(uri, body, DEFAULT_CONTENT_TYPE);
                break;
            case DELETE:
                atsdHttpResponse = delete(uri);
                break;
            default:
                atsdHttpResponse = null;
        }
        return atsdHttpResponse;
    }


    private AtsdHttpResponse get(String atsdMethod, ContentType contentType) throws IOException {
        return driver.get(dataPath + atsdMethod, contentType);
    }

    private AtsdHttpResponse post(String atsdMethod, String body, ContentType contentType) throws IOException {

        return driver.post(dataPath + atsdMethod, body, contentType);
    }

    private AtsdHttpResponse put(String atsdMethod, String body, ContentType contentType) throws IOException {

        return driver.put(dataPath + atsdMethod, body, contentType);
    }

    private AtsdHttpResponse patch(String atsdMethod, String body, ContentType contentType) throws IOException {
        return driver.patch(dataPath + atsdMethod, body, contentType);
    }

    private AtsdHttpResponse delete(String atsdMethod) throws IOException {
        return driver.delete(dataPath + atsdMethod);
    }
}
