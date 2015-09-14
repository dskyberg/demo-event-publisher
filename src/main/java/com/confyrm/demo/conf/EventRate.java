/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.conf;

public class EventRate {
    private String eventType;
    private int ratePerSecond;

    public EventRate() {
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getRatePerSecond() {
        return ratePerSecond;
    }

    public void setRatePerSecond(int ratePerSecond) {
        this.ratePerSecond = ratePerSecond;
    }
}
