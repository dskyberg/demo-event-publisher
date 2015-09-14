/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.impl;

import com.confyrm.demo.client.IConfyrmAuthClient;
import com.confyrm.demo.client.to.AuthErrorResponseTO;
import com.confyrm.demo.client.to.TokenResponseTO;
import com.confyrm.demo.exception.AuthException;
import com.confyrm.demo.utils.JsonUtil;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@ThreadSafe
public class ConfyrmAuthClient implements IConfyrmAuthClient {

    private static final String CREATE_TOKEN_REQUEST_FORMAT = "grant_type=password&client_id=confyrm-api&username=%s&password=%s";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CloseableHttpClient httpClient;

    private final String authUrl;

    public ConfyrmAuthClient(CloseableHttpClient httpClient, String authUrl) {
        this.httpClient = httpClient;
        this.authUrl = authUrl;
    }

    @Override
    public TokenResponseTO getToken(String userName, String password) throws Exception {
        logger.info("Requesting token for userName={}", userName);

        HttpPost request = prepareRequest(userName, password);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return processTokenResponse(response);
        }
    }

    private HttpPost prepareRequest(String userName, String password) throws UnsupportedEncodingException {
        String requestContent = String.format(CREATE_TOKEN_REQUEST_FORMAT, urlEncode(userName), urlEncode(password));
        StringEntity entity = new StringEntity(requestContent, ContentType.APPLICATION_FORM_URLENCODED);
        HttpPost request = new HttpPost(authUrl);
        request.setEntity(entity);
        return request;
    }

    private TokenResponseTO processTokenResponse(CloseableHttpResponse response) throws Exception {
        int responseCode = response.getStatusLine().getStatusCode();
        String responseContent = EntityUtils.toString(response.getEntity());

        switch (responseCode) {
            case 200:
                return getTokenFromResponseContent(responseContent);
            case 400:
                String errorMessage;
                try {
                    errorMessage = JsonUtil.fromJson(responseContent, AuthErrorResponseTO.class).toString();
                } catch (Exception e) {
                    errorMessage = responseContent;
                }
                throw new AuthException(errorMessage);
            default:
                throw new AuthException(String.format("AuthService return error: %s, response content: %s", responseCode, responseContent));
        }
    }

    private TokenResponseTO getTokenFromResponseContent(String responseContent) throws Exception {
        try {
            return JsonUtil.fromJson(responseContent, TokenResponseTO.class);
        } catch (IOException e) {
            throw new Exception("JSON parse error", e);
        }
    }

    private String urlEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }
}
