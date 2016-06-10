package com.axibase.tsd.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Dmitry Korchagin.
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String DEFAULT_CONFIG_FILE = "src/test/resources/client.properties";
    private static final String DEV_CONFIG_FILE = "src/test/resources/dev.client.properties";
    private static Config instance = null;
    private String login;
    private String password;
    private String protocol;
    private String serverName;
    private int httpPort;
    private int tcpPort;
    private String dataPath;
    private String metadataPath;

    public static Logger getLogger() {
        return logger;
    }

    public static String getDefaultConfigFile() {
        return DEFAULT_CONFIG_FILE;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getServerName() {
        return serverName;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getMetadataPath() {
        return metadataPath;
    }

    public static Config getInstance() {
        if (null == instance) {
            if(new File(DEV_CONFIG_FILE).exists()) {
                logger.debug("Using dev.client.properties for config");
                instance = new Config(DEV_CONFIG_FILE);
            } else {
                logger.debug("Using client.properties for config");
                instance = new Config(DEFAULT_CONFIG_FILE);
            }
        }
        return instance;
    }

    private Config(String configPath) {
        logger.debug("Load client properties from file: {}", configPath);
        Properties clientProperties = new Properties();
        try (InputStream stream = new FileInputStream(configPath)) {
            clientProperties.load(stream);
        } catch (Exception e) {
            logger.error("Fail to load client properties");
            e.printStackTrace();
        }

        login = load("login", clientProperties, null);
        password = load("password", clientProperties, null);
        protocol = load("protocol", clientProperties, null);
        serverName = load("serverName", clientProperties, null);
        httpPort = Integer.parseInt(load("httpPort", clientProperties, null));
        tcpPort = Integer.parseInt(load("tcpPort", clientProperties, null));
        dataPath = load("dataPath", clientProperties, null);
        metadataPath = load("metadataPath", clientProperties, null);
        logger.debug(this.toString());
    }

    private static String load(String name, Properties clientProperties, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null) {
            value = clientProperties.getProperty(name);
            if (value == null) {
                if (defaultValue == null) {
                    logger.error("Could not find required property: {}", name);
                    throw new IllegalStateException(name + " property is null");
                } else {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return "Config{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", protocol='" + protocol + '\'' +
                ", serverName='" + serverName + '\'' +
                ", httpPort=" + httpPort +
                ", tcpPort=" + tcpPort +
                ", dataPath='" + dataPath + '\'' +
                ", metadataPath='" + metadataPath + '\'' +
                '}';
    }
}
