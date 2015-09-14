/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.client.IConfyrmApiClient;
import com.confyrm.demo.client.to.ConfyrmResponseTO;
import com.confyrm.demo.client.to.EventRequestTO;
import com.confyrm.demo.client.to.EventResponseTO;
import com.confyrm.demo.model.ConfyrmEvent;
import com.confyrm.demo.service.IAuthService;
import com.confyrm.demo.service.IEventSender;
import com.confyrm.demo.service.IEventStore;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EventSender implements IEventSender {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IEventStore store;
    private final IConfyrmApiClient apiClient;
    private final IAuthService authService;
    private final int batchSize;
    private final EventThrottler throttler;

    private final ThreadPoolExecutor sendEventsWorkerPool;

    private volatile boolean running = false;

    @Inject
    public EventSender(
            IEventStore store,
            IConfyrmApiClient apiClient,
            IAuthService authService,
            @Named("rateOfEventsPerSecond") int ratePerSecond) {

        this.store = store;
        this.apiClient = apiClient;
        this.authService = authService;
        this.batchSize = 10;

        LinkedBlockingDeque<Runnable> taskQueue = new LinkedBlockingDeque<>();
        this.sendEventsWorkerPool = new ThreadPoolExecutor(ratePerSecond, ratePerSecond, 30, TimeUnit.SECONDS, taskQueue);

        this.throttler = new EventThrottler(ratePerSecond, this::send);
    }

    @Override
    public synchronized void start() {
        if (!running) {
            logger.info("Starting...");
            running = true;
            doStart();
        }
    }

    private void doStart() {
        Thread worker = new Thread(() -> {
            while (running) {
                try {
                    List<ConfyrmEvent> events = store.getPendingEvents(batchSize);
                    sendEventsWorkerPool.submit(() -> {
                        try {
                            boolean resend = throttler.handle(events);
                            if (resend) {
                                store.putToFront(events);
                            }
                        } catch (Exception e) {
                            logger.error(String.format("Error sending %s events", events.size()), e);
                            store.putToFront(events);
                        }
                    });
                } catch (InterruptedException e) {
                    logger.info("Sender was interrupted");
                } catch (Exception e) {
                    logger.error("Error in event sender", e);
                }
            }
        });
        worker.setDaemon(true);
        worker.start();
    }

    private boolean send(List<ConfyrmEvent> events) throws Exception {
        String token = authService.getToken();
        List<EventRequestTO> requestTO = events.stream()
                .map(event -> new EventRequestTO(event.getEventType(), event.getExternalRef(), event.getSubscriptionRef()))
                .collect(Collectors.toList());

        EventResponseTO responseTO = apiClient.sendEvents(token, requestTO);
        Optional<ConfyrmResponseTO> content = responseTO.getContent();
        String transactionId = content.map(ConfyrmResponseTO::getTransactionId).orElse("");

        boolean resend;
        switch (responseTO.getResponseCode()) {
            case 200:
                // all OK
                logger.info("[{}] {} event(s) was published", transactionId, events.size());
                resend = false;
                break;

            case 400:
                // Invalid data in request
                logger.error("Confyrm service unable to process request: {}", content.map(ConfyrmResponseTO::toString).orElse(""));
                resend = false;
                break;

            case 401:
                logger.error("Unauthorized, response: {}", content.map(ConfyrmResponseTO::toString).orElse(""));
                resend = false;
                break;

            case 403:
                logger.error("Forbidden, response: {}", content.map(ConfyrmResponseTO::toString).orElse(""));
                resend = false;
                break;

            case 500:
                // Confyrm service internal error
                resend = true;

                // check if service is overloaded
                boolean isOverloaded = content.map(c -> c.getStatus().getCode().equals("OVR")).orElse(false);
                if (isOverloaded) {
                    logger.error("Confyrm service is overloaded, response: ", content.map(ConfyrmResponseTO::toString).orElse(""));
                }

                break;

            default:
                // unexpected error
                logger.error("Unexpected response code: {}, response content: {}", responseTO.getResponseCode(), content.map(ConfyrmResponseTO::toString).orElse(""));
                resend = true;
        }
        return resend;
    }
}
