/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service.impl;

import com.confyrm.demo.client.impl.ConfyrmAuthClient;
import com.confyrm.demo.client.to.TokenResponseTO;
import com.confyrm.demo.service.IAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AuthService implements IAuthService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ConfyrmAuthClient confyrmAuthClient;

    private String userName;

    private String password;

    private String token;

    private ScheduledThreadPoolExecutor updateTokenExecutorPool = new ScheduledThreadPoolExecutor(1);

    public AuthService(ConfyrmAuthClient confyrmAuthClient, String userName, String password) {
        this.confyrmAuthClient = confyrmAuthClient;
        this.userName = userName;
        this.password = password;
        scheduleUpdateTokenTask(0, TimeUnit.NANOSECONDS);
    }

    @Override
    public String getToken() throws Exception {
        synchronized (this) {
            if (token == null) {
                //waiting until token become available
                logger.info("Token is not available, waiting...");
                wait();
            }
            return token;
        }
    }

    private void scheduleUpdateTokenTask(long delay, TimeUnit delayTimeUnit) {
        updateTokenExecutorPool.schedule((Runnable) () -> {
            try {
                logger.info("Receiving new token...");
                TokenResponseTO response = confyrmAuthClient.getToken(userName, password);
                synchronized (this) {
                    logger.info("Received new token");
                    token = response.getAccessToken();
                    scheduleUpdateTokenTask(response.getExpiresIn() / 2, TimeUnit.SECONDS);
                    notifyAll();
                }
            } catch (Exception e) {
                synchronized (this) {
                    token = null;
                }
                logger.warn("Unable receive token, will try after 5 seconds", e);
                scheduleUpdateTokenTask(5, TimeUnit.SECONDS);
            }
        }, delay, delayTimeUnit);
    }
}
