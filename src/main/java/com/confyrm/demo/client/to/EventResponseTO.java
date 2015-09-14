/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.client.to;

import java.util.Optional;

public class EventResponseTO {

    private final int responseCode;

    private final Optional<ConfyrmResponseTO> content;

    public EventResponseTO(int responseCode, Optional<ConfyrmResponseTO> content) {
        this.responseCode = responseCode;
        this.content = content;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Optional<ConfyrmResponseTO> getContent() {
        return content;
    }
}
