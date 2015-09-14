/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service;

import com.confyrm.demo.model.ConfyrmEvent;

import java.util.List;

/**
 * Storage for unsent events
 */
public interface IEventStore {

    /**
     * Get first count events
     */
    List<ConfyrmEvent> getPendingEvents(int count) throws InterruptedException;

    /**
     * Put events to the front of queue
     */
    void putToFront(List<ConfyrmEvent> events);

    /**
     * Put evetnts to the ent of queue
     */
    void putToBack(List<ConfyrmEvent> events);

    /**
     * Check if store is empty
     */
    boolean isEmpty();
}
