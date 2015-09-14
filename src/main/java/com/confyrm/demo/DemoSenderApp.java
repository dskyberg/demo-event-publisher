/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo;

import com.confyrm.demo.conf.AppConfig;
import com.confyrm.demo.conf.DemoModule;
import com.confyrm.demo.conf.EventConfig;
import com.confyrm.demo.service.IEventProducer;
import com.confyrm.demo.service.IEventSender;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DemoSenderApp {

    private final Logger logger = LoggerFactory.getLogger(DemoSenderApp.class);

    private final AppConfig appConfig;

    private final EventConfig eventConfig;

    public DemoSenderApp(AppConfig appConfig, EventConfig eventConfig) {
        this.appConfig = appConfig;
        this.eventConfig = eventConfig;
    }

    public void start() throws IOException {
        logger.info("Started");

        Injector injector = Guice.createInjector(new DemoModule(appConfig, eventConfig));
        IEventProducer producer = injector.getInstance(IEventProducer.class);
        producer.start();

        IEventSender sender = injector.getInstance(IEventSender.class);
        sender.start();
    }
}
