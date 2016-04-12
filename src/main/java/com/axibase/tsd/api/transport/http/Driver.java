package com.axibase.tsd.api.transport.http;

import java.io.IOException;

/**
 * @author Dmitry Korchagin.
 */
interface Driver {


    abstract AtsdResponse get(String atsdMethod) throws IOException;
    abstract AtsdResponse post(String atsdMethod, String body) throws IOException;
    abstract AtsdResponse put(String atsdMethod, String body) throws IOException;
    abstract AtsdResponse patch(String atsdMethod, String body) throws IOException;
    abstract AtsdResponse delete(String atsdMethod) throws IOException;
}
