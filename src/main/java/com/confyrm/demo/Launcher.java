/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo;

import com.confyrm.demo.conf.AppConfig;
import com.confyrm.demo.conf.EventConfig;

public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Expected three parameters: path to Application properties, path to Event configuration, path to file with Identities");
        } else {
            String appPropertiesFileName = args[0];
            String eventConfigurationFileName = args[1];
            String identitiesFileName = args[2];
            AppConfig appConfig = new AppConfig(appPropertiesFileName);
            EventConfig eventConfig = new EventConfig(eventConfigurationFileName, identitiesFileName);
            DemoSenderApp app = new DemoSenderApp(appConfig, eventConfig);
            app.start();
        }
    }
}
