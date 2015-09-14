/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service;

import com.confyrm.demo.model.ConfyrmEvent;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IEventHandler<Resp> {

    Resp handle(List<ConfyrmEvent> events) throws Exception;
}
