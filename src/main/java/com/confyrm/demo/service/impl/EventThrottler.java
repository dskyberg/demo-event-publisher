/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.model.ConfyrmEvent;
import com.confyrm.demo.service.IEventHandler;
import com.google.common.util.concurrent.RateLimiter;

import java.util.List;

/**
 * Provide the necessary rate of event processing
 */
public class EventThrottler {

    private final IEventHandler<Boolean> targetService;
    private final RateLimiter rateLimiter;

    public EventThrottler(int ratePerSecond, IEventHandler<Boolean> targetService) {
        this.rateLimiter = RateLimiter.create(ratePerSecond);
        this.targetService = targetService;
    }

    public Boolean handle(List<ConfyrmEvent> events) throws Exception {
        rateLimiter.acquire(events.size());
        return targetService.handle(events);
    }
}
