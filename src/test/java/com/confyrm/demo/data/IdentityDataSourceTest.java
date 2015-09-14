/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.data;

import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IdentityDataSourceTest {

    @Test
    public void testReadNonEmptyIdentities() throws Exception {
        String identitiesResourceName = "non-empty-identities.csv";
        try (Reader identitiesSource = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(identitiesResourceName))) {
            List<Identity> identities = IdentityDataSource.readIdentities(identitiesSource);
            assertEquals(identities.size(), 3);
            assertEquals(identities.get(0), new Identity("usr1@domain1", "usr1_subscription_ref"));
            assertEquals(identities.get(1), new Identity("usr2@domain2", null));
            assertEquals(identities.get(2), new Identity("usr3@domain3", "usr3_subscription_ref"));
        }
    }

    @Test
    public void testReadEmptyIdentities() throws Exception {
        String identitiesResourceName = "empty-identities.csv";
        try (Reader identitiesSource = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(identitiesResourceName))) {
            List<Identity> identities = IdentityDataSource.readIdentities(identitiesSource);
            assertEquals(identities.size(), 0);
        }
    }

    @Test
    public void testReadBrokenIdentities() throws Exception {
        String identitiesResourceName = "broken-identities.csv";
        try (Reader identitiesSource = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(identitiesResourceName))) {
            List<Identity> identities = IdentityDataSource.readIdentities(identitiesSource);
            assertEquals(identities.size(), 1);
            assertEquals(identities.get(0), new Identity("usr4@domain4", null));
        }
    }
}

