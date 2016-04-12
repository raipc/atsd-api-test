package com.axibase.tsd.api.transport.http;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
public class RequestSender {
    private Driver driver;
    private String dataPath;
    private String metaDataPath;

    public RequestSender(Driver driver, String dataPath, String metaDataPath) {
        this.driver = driver;
        this.dataPath = dataPath;
        this.metaDataPath = metaDataPath;
    }


    public AtsdResponse get(String atsdMethod) throws IOException {
        return driver.get(dataPath + atsdMethod);
    };

    public AtsdResponse post(String atsdMethod, String body) throws IOException {
        return driver.post(dataPath + atsdMethod, body);
    };

    public AtsdResponse put(String atsdMethod, String body) throws IOException {
        return driver.put(dataPath + atsdMethod, body);
    };

    public AtsdResponse patch(String atsdMethod, String body) throws IOException {
        return driver.patch(dataPath + atsdMethod, body);
    };

    public AtsdResponse delete(String atsdMethod) throws IOException {
        return driver.delete(dataPath + atsdMethod);
    };
}
