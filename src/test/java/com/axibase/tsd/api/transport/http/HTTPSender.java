package com.axibase.tsd.api.transport.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
public class HTTPSender {
    private static final Logger logger = LoggerFactory.getLogger(HTTPSender.class);

    private HTTPClient driver;
    private String dataPath;
    private String metaDataPath;

    public HTTPSender(HTTPClient driver, String dataPath, String metaDataPath) {
        this.driver = driver;
        this.dataPath = dataPath;
        this.metaDataPath = metaDataPath;
    }

    public AtsdHttpResponse send(HTTPMethod method, String uri, String body) throws IOException {
        logger.debug("> METHOD: {}", method);
        AtsdHttpResponse atsdHttpResponse;
        switch (method) {
            case GET:
                atsdHttpResponse = get(uri);
                break;
            case POST:
                atsdHttpResponse = post(uri, body);
                break;
            case PUT:
                atsdHttpResponse = put(uri, body);
                break;
            case PATH:
                atsdHttpResponse = patch(uri, body);
                break;
            case DELETE:
                atsdHttpResponse = delete(uri);
                break;
            default:
                atsdHttpResponse = null;
        }
        return atsdHttpResponse;
    }


    private AtsdHttpResponse get(String atsdMethod) throws IOException {
        return driver.get(dataPath + atsdMethod);
    }

    private AtsdHttpResponse post(String atsdMethod, String body) throws IOException {

        return driver.post(dataPath + atsdMethod, body);
    }

    private AtsdHttpResponse put(String atsdMethod, String body) throws IOException {

        return driver.put(dataPath + atsdMethod, body);
    }

    private AtsdHttpResponse patch(String atsdMethod, String body) throws IOException {
        return driver.patch(dataPath + atsdMethod, body);
    }

    private AtsdHttpResponse delete(String atsdMethod) throws IOException {
        return driver.delete(dataPath + atsdMethod);
    }
}
