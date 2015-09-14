/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.to;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Authorization service error response
 */
public class AuthErrorResponseTO {

    /**
     * Kind of error
     */
    private String error;

    /**
     * Error description
     */
    private String description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("error_description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AuthErrorResponseTO{" + "error='" + error + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
