/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.model.ConfyrmEvent;
import com.confyrm.demo.service.IEventProducer;
import com.confyrm.demo.service.IEventStore;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventProducer implements IEventProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IEventStore eventsStore;
    private final EventGenerator eventGenerator;
    private final ScheduledThreadPoolExecutor produceEventsScheduler;
    private final int rateOfEventsPerSecond;

    private boolean running = false;

    @Inject
    public EventProducer(
            IEventStore eventsStore,
            EventGenerator eventGenerator,
            @Named("rateOfEventsPerSecond") int rateOfEventsPerSecond) {
        this.eventsStore = eventsStore;
        this.eventGenerator = eventGenerator;
        this.rateOfEventsPerSecond = rateOfEventsPerSecond;
        this.produceEventsScheduler = new ScheduledThreadPoolExecutor(rateOfEventsPerSecond);
    }

    public synchronized void start() {
        if (!running) {
            running = true;
            doStart();
        }
    }

    private void doStart() {
        Thread worker = new Thread(() -> {
            Random rnd = new Random();
            while (running) {
                try {
                    long startTime = System.currentTimeMillis();
                    for (int eventCounter = 0; eventCounter < rateOfEventsPerSecond; eventCounter++) {
                        int sendDelay = rnd.nextInt(1000);
                        List<ConfyrmEvent> events = Collections.singletonList(eventGenerator.nextEvent());
                        produceEventsScheduler.schedule(() -> eventsStore.putToBack(events), sendDelay, TimeUnit.MILLISECONDS);
                    }
                    long workedTime = System.currentTimeMillis() - startTime;
                    Thread.sleep(1000 - workedTime);
                } catch (InterruptedException e) {
                    logger.info("Event producer was interrupted");
                } catch (Exception e) {
                    logger.error("Error in event producer", e);
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }
}
