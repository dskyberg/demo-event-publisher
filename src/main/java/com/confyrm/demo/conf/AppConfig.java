/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private final String appPropertiesFileName;

    private final String apiUrl;

    private final String authUrl;

    private final String login;

    private final String password;

    private final int httpConnections;

    public AppConfig(String appPropertiesFileName) throws Exception {
        this.appPropertiesFileName = appPropertiesFileName;

        InputStream is = null;
        try {
            is = new FileInputStream(new File(appPropertiesFileName));
            Properties props = new Properties();
            props.load(is);
            apiUrl = getProperty(props, "api.url");
            authUrl = getProperty(props, "auth.url");
            login = getProperty(props, "auth.login");
            password = getProperty(props, "auth.password");
            httpConnections = Integer.parseInt(getProperty(props, "http.connections"));
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getLogin() throws Exception {
        if ("*****".equals(login)) {
            throw new Exception("auth.login is not specified in " + appPropertiesFileName);
        }
        return login;
    }

    public String getPassword() throws Exception {
        if ("*****".equals(password)) {
            throw new Exception("auth.password is not specified in " + appPropertiesFileName);
        }
        return password;
    }

    public int getHttpConnections() {
        return httpConnections;
    }

    private String getProperty(Properties props, String propertyName) throws Exception {
        if (!props.containsKey(propertyName)) {
            throw new Exception(String.format("Property with key='%s' not found", propertyName));
        }
        return props.getProperty(propertyName);
    }
}
