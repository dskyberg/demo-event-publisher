/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service;

/**
 * Event sender service. It take events from EventStore and publish them to Confyrm
 */
public interface IEventSender {

    void start();
}
