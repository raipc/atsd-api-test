package com.axibase.tsd.api.method;

import com.axibase.tsd.api.Config;
import com.axibase.tsd.api.transport.http.HTTPClientPure;
import com.axibase.tsd.api.transport.http.HTTPSender;
import com.axibase.tsd.api.transport.tcp.TCPSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry Korchagin.
 */
public abstract class Method {
    private static final Logger logger = LoggerFactory.getLogger(Method.class);
    protected static HTTPSender httpSender;
    protected static TCPSender tcpSender;

    protected static void prepareRequestSender() {
        Config config = Config.getInstance();
        HTTPClientPure driver = new HTTPClientPure(config.getProtocol(), config.getServerName(), config.getHttpPort(), config.getLogin(), config.getPassword());
        httpSender = new HTTPSender(driver, config.getDataPath(), config.getMetadataPath());
        tcpSender = new TCPSender(config.getServerName(), config.getTcpPort());
    }

}
