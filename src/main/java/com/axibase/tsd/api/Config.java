package com.axibase.tsd.api;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Dmitry Korchagin.
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final String DEFAULT_CONFIG_FILE = "src/main/resources/client.properties";
    private static Config instance = null;
    private String login;
    private String password;
    private String protocol;
    private String serverName;
    private int serverPort;
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

    public int getServerPort() {
        return serverPort;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getMetadataPath() {
        return metadataPath;
    }

    public static Config getInstance() {
        if(null == instance) {
            instance = new Config();
        }
        return instance;
    }

    private Config() {
        this(DEFAULT_CONFIG_FILE);
    }

    private Config(String configPath) {
        logger.debug("Load client properties from file: {}", configPath);
        Properties clientProperties = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(configPath);
            clientProperties.load(stream);
        } catch(Exception e) {
            logger.error("Fail to load client properties");
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(stream);
        }

        login = load("login", clientProperties, null);
        password = load("password", clientProperties, null);
        protocol = load("protocol", clientProperties, null);
        serverName = load("serverName", clientProperties, null);
        serverPort = Integer.parseInt(load("serverPort", clientProperties, null));
        dataPath = load("dataPath", clientProperties, null);
        metadataPath = load("metadataPath", clientProperties, null);
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
                ", serverPort='" + serverPort + '\'' +
                ", dataPath='" + dataPath + '\'' +
                ", metadataPath='" + metadataPath + '\'' +
                '}';
    }
}
