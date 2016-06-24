package com.axibase.tsd.api.method;

import com.axibase.tsd.api.Config;
import com.axibase.tsd.api.transport.tcp.TCPSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.logging.Level;


public abstract class BaseMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static TCPSender tcpSender;
    protected static ObjectMapper jacksonMapper;
    protected static WebTarget httpApiResource;
    protected static WebTarget httpRootResource;
    protected static Config config;

    static {
        java.util.logging.LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST);
        prepare();
    }

    private static void prepare(){
        try {
            config = Config.getInstance();
            ClientConfig clientConfig = new ClientConfig();
            HttpAuthenticationFeature httpAuthenticationFeature = HttpAuthenticationFeature.basic(config.getLogin(), config.getPassword());
            clientConfig.register(httpAuthenticationFeature);
            clientConfig.register(MultiPartFeature.class);
            clientConfig.register(new LoggingFeature());
            httpRootResource = ClientBuilder.newClient(clientConfig).target(UriBuilder.fromPath("")
                    .scheme(config.getProtocol())
                    .host(config.getServerName())
                    .port(config.getHttpPort())
                    .build());
            httpApiResource = httpRootResource.path(config.getApiPath());
            tcpSender = new TCPSender(config.getServerName(), config.getTcpPort());
            jacksonMapper = new ObjectMapper();
            jacksonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssXXX"));
        } catch (FileNotFoundException fne){
            logger.error("Failed prepare BaseMethod class. Reason: {}", fne.getMessage());
            throw new RuntimeException(fne);
        }

    }
}