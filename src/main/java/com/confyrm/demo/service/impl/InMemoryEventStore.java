/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.model.ConfyrmEvent;
import com.confyrm.demo.service.IEventStore;

import java.util.ArrayList;
import java.util.List;

public class InMemoryEventStore implements IEventStore {

    private List<ConfyrmEvent> store = new ArrayList<>();

    public InMemoryEventStore() {
    }

    @Override
    public synchronized List<ConfyrmEvent> getPendingEvents(int count) throws InterruptedException {
        if (store.isEmpty()) {
            wait();
        }

        int realCount = count;
        if (realCount > store.size()) {
            realCount = store.size();
        }

        List<ConfyrmEvent> result = new ArrayList<>(store.subList(0, realCount));
        store = store.subList(realCount, store.size());
        return result;
    }

    @Override
    public synchronized void putToFront(List<ConfyrmEvent> events) {
        List<ConfyrmEvent> tmp = new ArrayList<>(store.size() + events.size());
        tmp.addAll(events);
        tmp.addAll(store);
        store = tmp;
        notify();
   }

    @Override
    public synchronized void putToBack(List<ConfyrmEvent> events) {
        store.addAll(events);
        notify();
    }

    @Override
    public  synchronized boolean isEmpty() {
        return store.isEmpty();
    }
}
