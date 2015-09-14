/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.data;

/**
 * Information about user account.
 */
public class Identity {

    /**
     * An externally visible reference, such as an email, that is provided by the Event Publisher
     */
    private final String externalRef;

    /**
     * An optional reference that is provided by the Event Publisher for the externalRef on which the Event is being raised
     */
    private final String subscriptionRef;

    public Identity(String externalRef, String subscriptionRef) {
        this.externalRef = externalRef;
        this.subscriptionRef = subscriptionRef;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public String getSubscriptionRef() {
        return subscriptionRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity that = (Identity) o;

        if (!externalRef.equals(that.externalRef)) return false;
        return !(subscriptionRef != null ? !subscriptionRef.equals(that.subscriptionRef) : that.subscriptionRef != null);

    }

    @Override
    public int hashCode() {
        int result = externalRef.hashCode();
        result = 31 * result + (subscriptionRef != null ? subscriptionRef.hashCode() : 0);
        return result;
    }
}
