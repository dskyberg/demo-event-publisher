/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.to;

/**
 * Response of Confyrm API
 */
public class ConfyrmResponseTO {

    /**
     * Status of processed request
     */
    private StatusTO status;

    /**
     * Transaction Id of request
     */
    private String transactionId;

    /**
     * Additional information
     */
    private String content;

    public ConfyrmResponseTO() {
    }

    public StatusTO getStatus() {
        return status;
    }

    public void setStatus(StatusTO status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ConfyrmResponseTO{" + "status=" + status +
                ", transactionId='" + transactionId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
