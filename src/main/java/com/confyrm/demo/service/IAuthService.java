/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.service;

/**
 * Provide access to OAuth2 token
 */
public interface IAuthService {

    String getToken() throws Exception;
}
