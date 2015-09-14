/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.conf.EventRate;
import com.confyrm.demo.data.Identity;
import com.confyrm.demo.model.ConfyrmEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generate sequence of events with desired frequency of occurrence
 */
public class EventGenerator {

    private final Identity[] identities;

    private final String[] frequencyListOfEventTypes;

    private final AtomicInteger identityPointer = new AtomicInteger(0);

    private final AtomicInteger eventTypePointer = new AtomicInteger(0);

    /**
     * Provides the necessary frequency of events
     * @param identities the list of accounts for which will be generated the event
     * @param eventRates the list of event types and their frequency of occurrence
     */
    public EventGenerator(List<Identity> identities, List<EventRate> eventRates) {

        this.identities = new Identity[identities.size()];
        identities.toArray(this.identities);

        // creating frequency list of event types
        List<String> eventTypes = new ArrayList<>();
        eventRates.forEach((eventRate) -> {
            for (int i = 0; i < eventRate.getRatePerSecond(); i++) {
                eventTypes.add(eventRate.getEventType());
            }
        });
        Collections.shuffle(eventTypes, new Random());

        frequencyListOfEventTypes = new String[eventTypes.size()];
        eventTypes.toArray(frequencyListOfEventTypes);

    }

    /**
     * Creates the event in accordance with its frequency
     */
    public ConfyrmEvent nextEvent() {
        int identityOffset = identityPointer.getAndIncrement() % identities.length;
        Identity identity = identities[identityOffset];

        int eventTypeOffset = eventTypePointer.getAndIncrement() % frequencyListOfEventTypes.length;
        String eventType = frequencyListOfEventTypes[eventTypeOffset];

        return new ConfyrmEvent(eventType, identity.getExternalRef(), identity.getSubscriptionRef());
    }
}
