package com.axibase.tsd.api.method;

import com.axibase.tsd.api.Config;
import com.axibase.tsd.api.Util;
import com.axibase.tsd.api.transport.http.HTTPClientPure;
import com.axibase.tsd.api.transport.http.HTTPSender;
import com.axibase.tsd.api.transport.tcp.TCPSender;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * @author Dmitry Korchagin.
 */
public abstract class Method {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static HTTPSender httpSender;
    protected static TCPSender tcpSender;

    protected static void prepareRequestSender() {
        Config config = Config.getInstance();
        HTTPClientPure driver = new HTTPClientPure(config.getProtocol(), config.getServerName(), config.getHttpPort(), config.getLogin(), config.getPassword());
        httpSender = new HTTPSender(driver, config.getDataPath(), config.getMetadataPath());
        tcpSender = new TCPSender(config.getServerName(), config.getTcpPort());
    }

    abstract public void checkDataset() throws IOException;


    protected JSONArray getDataset(final String datasetPath) throws IOException {
        logger.debug("Starting to parse Dataset.\nDataset file: {}", datasetPath);

        JSONArray dataset;
        try {
            dataset = (JSONArray) new JSONParser().parse(Util.getFileContent(datasetPath));
            logger.info("Dataset parsed successfully, query set count: {}", dataset.size());
        } catch (ParseException e) {
            logger.error("Fail to parse Dataset");
            return null;
        }
        return dataset;
    }

}
