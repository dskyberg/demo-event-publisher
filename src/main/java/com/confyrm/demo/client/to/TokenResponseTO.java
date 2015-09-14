/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponseTO {

    /**
     * Requested token
     */
    private String accessToken;

    /**
     * Type of token
     */
    private String tokenType;

    /**
     * The time after which the token will be expired.
     */
    private int expiresIn;

    public TokenResponseTO() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
