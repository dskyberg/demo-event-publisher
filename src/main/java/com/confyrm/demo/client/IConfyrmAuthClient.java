/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client;

import com.confyrm.demo.client.to.TokenResponseTO;

/**
 * Authentication service client
 */
public interface IConfyrmAuthClient {

    /**
     * Generate OAuth2 token for
     *
     * @param userName client user name
     * @param password client password
     * @return token response
     * @throws Exception if errors happened during operation
     */
    TokenResponseTO getToken(String userName, String password) throws Exception;
}
