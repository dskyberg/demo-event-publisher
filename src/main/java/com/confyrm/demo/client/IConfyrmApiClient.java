/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client;

import com.confyrm.demo.client.to.EventRequestTO;
import com.confyrm.demo.client.to.EventResponseTO;
import com.confyrm.demo.exception.EventException;

import java.util.List;

/**
 * Confyrm API client for EventSender
 */
public interface IConfyrmApiClient {

    /**
     * Send event to Confyrm service
     * @param authToken authorization token
     * @param events list of events
     * @return response from Confyrm API
     * @throws EventException if errors happened during operation
     */
    EventResponseTO sendEvents(String authToken, List<EventRequestTO> events) throws EventException;
}
