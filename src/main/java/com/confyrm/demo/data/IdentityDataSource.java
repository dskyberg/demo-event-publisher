/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class. It needs for reads and parse the data with the user accounts
 */
public class IdentityDataSource {

    private static final Logger logger = LoggerFactory.getLogger(IdentityDataSource.class);

    /**
     * Reads and parse the data with the user accounts
     *
     * @param identitiesSource the source from which will be read user accounts
     * @return List of user identities
     * @throws IOException If an I/O error occurs
     */
    public static List<Identity> readIdentities(Reader identitiesSource) throws IOException {
        List<Identity> identities = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(identitiesSource)) {
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.length() > 0) {
                    try {
                        Identity identity = parseIdentity(line);
                        identities.add(identity);
                    } catch (Exception e) {
                        logger.warn(String.format("Error parsing identity from '%s' at line %s: %s", lineNumber, line, e.getMessage()), e);
                        e.printStackTrace();
                    }
                }
            }
        }
        return identities;
    }

    private static Identity parseIdentity(String line) throws Exception {
        String[] parts = line.split(";");
        if (parts.length != 1 && parts.length != 2) {
            throw new Exception("Identity string should be like 'externalRef' or 'externalRef;subscriptionRef'");
        }
        String externalRef = parts[0];
        String subscriptionRef = parts.length == 2 ? parts[1] : null;
        return new Identity(externalRef, subscriptionRef);
    }
}
