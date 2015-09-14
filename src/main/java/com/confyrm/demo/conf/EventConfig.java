/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.conf;

import com.confyrm.demo.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EventConfig {

    private final String identitiesFileName;

    private final List<EventRate> rates;

    public EventConfig(String eventConfigurationFileName, String identitiesFileName) throws Exception {

        try {
            rates = JsonUtil.fromJson(
                    new String(Files.readAllBytes(Paths.get(eventConfigurationFileName)), StandardCharsets.UTF_8),
                    new TypeReference<List<EventRate>>() {
                    });
        } catch (IOException e) {
            throw new Exception(String.format("Unable to read and parse config from '%s'", eventConfigurationFileName), e);
        }

        if (!new File(identitiesFileName).exists()) {
            throw new Exception(String.format("File does not exists: %s", identitiesFileName));
        }
        this.identitiesFileName = identitiesFileName;
    }

    public String getIdentitiesFileName() {
        return identitiesFileName;
    }

    public List<EventRate> getRates() {
        return rates;
    }

    public int getTotalRate() {
        return rates.stream().mapToInt(EventRate::getRatePerSecond).sum();
    }
}
