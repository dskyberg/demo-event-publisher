/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.to;

/**
 * Represent information about status of response to Confyrm API
 */
public class StatusTO {

    /**
     * Status code
     */
    private String code;

    /**
     * Description of status
     */
    private String description;

    /**
     * Additional information of status
     */
    private String context;

    public StatusTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "StatusTO{" + "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
