/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.model.ConfyrmEvent;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryEventStoreTest {

    @Test
    public void testGetPendingEvents() throws Exception {
        InMemoryEventStore store = new InMemoryEventStore();

        store.putToFront(Arrays.asList(
                new ConfyrmEvent("eventType1", "externalRef1", null),
                new ConfyrmEvent("eventType2", "externalRef2", null)));

        List<ConfyrmEvent> pending1 = store.getPendingEvents(1);
        assertEquals(pending1.size(), 1);
        assertEquals(pending1.get(0).getEventType(), "eventType1");

        List<ConfyrmEvent> pending2 = store.getPendingEvents(1);
        assertEquals(pending2.size(), 1);
        assertEquals(pending2.get(0).getEventType(), "eventType2");

        assertTrue(store.isEmpty());
    }

    @Test
    public void testPutToFront() throws Exception {
        InMemoryEventStore store = new InMemoryEventStore();

        store.putToFront(Arrays.asList(
                new ConfyrmEvent("eventType1", "externalRef1", null),
                new ConfyrmEvent("eventType2", "externalRef2", null)));
        store.putToFront(Collections.singletonList(new ConfyrmEvent("eventType3", "externalRe3", null)));

        List<ConfyrmEvent> pending = store.getPendingEvents(3);
        assertEquals(pending.size(), 3);
        assertEquals(pending.get(0).getEventType(), "eventType3");
        assertEquals(pending.get(1).getEventType(), "eventType1");
        assertEquals(pending.get(2).getEventType(), "eventType2");

        assertTrue(store.isEmpty());
    }

    @Test
    public void testPutToBack() throws Exception {
        InMemoryEventStore store = new InMemoryEventStore();

        store.putToFront(Arrays.asList(
                new ConfyrmEvent("eventType1", "externalRef1", null),
                new ConfyrmEvent("eventType2", "externalRef2", null)));

        store.putToBack(Collections.singletonList(new ConfyrmEvent("eventType3", "externalRe3", null)));

        List<ConfyrmEvent> pending = store.getPendingEvents(3);
        assertEquals(pending.size(), 3);
        assertEquals(pending.get(0).getEventType(), "eventType1");
        assertEquals(pending.get(1).getEventType(), "eventType2");
        assertEquals(pending.get(2).getEventType(), "eventType3");

        assertTrue(store.isEmpty());
    }
}