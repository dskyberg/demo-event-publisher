/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.impl;

import com.confyrm.demo.client.IConfyrmApiClient;
import com.confyrm.demo.client.to.ConfyrmResponseTO;
import com.confyrm.demo.client.to.EventRequestTO;
import com.confyrm.demo.client.to.EventResponseTO;
import com.confyrm.demo.exception.EventException;
import com.confyrm.demo.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ConfyrmApiClient implements IConfyrmApiClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String apiUrl;

    private final CloseableHttpClient httpClient;

    public ConfyrmApiClient(CloseableHttpClient httpClient, String apiUrl) {
        this.apiUrl = apiUrl;
        this.httpClient = httpClient;
    }

    @Override
    public EventResponseTO sendEvents(String authToken, List<EventRequestTO> events) throws EventException {
        try {
            HttpPost post = prepareRequest(authToken, events);
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                return processEventResponse(response);
            }
        } catch (Exception e) {
            throw new EventException("Error sending request to " + apiUrl, e);
        }
    }

    private HttpPost prepareRequest(String authToken, List<EventRequestTO> events) throws JsonProcessingException {
        String requestContent = JsonUtil.toJson(events);
        logger.info("Publishing {} event(s): {}", events.size(), requestContent);

        StringEntity entity = new StringEntity(requestContent, ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost(apiUrl);
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authToken);
        request.setEntity(entity);
        return request;
    }

    private EventResponseTO processEventResponse(CloseableHttpResponse response) {
        int responseCode = response.getStatusLine().getStatusCode();
        Optional<ConfyrmResponseTO> responseTO = Optional.empty();
        try {
            String responseContent = EntityUtils.toString(response.getEntity());
            responseTO = Optional.of(JsonUtil.fromJson(responseContent, ConfyrmResponseTO.class));
        } catch (IOException e) {
            logger.warn("Unable read response content", e);
        }
        return new EventResponseTO(responseCode, responseTO);
    }
}