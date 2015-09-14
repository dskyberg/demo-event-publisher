/**
 * Copyright 2015 by Confyrm Inc.
 */
package com.confyrm.demo.conf;

import com.confyrm.demo.client.IConfyrmApiClient;
import com.confyrm.demo.client.impl.ConfyrmApiClient;
import com.confyrm.demo.client.impl.ConfyrmAuthClient;
import com.confyrm.demo.data.Identity;
import com.confyrm.demo.data.IdentityDataSource;
import com.confyrm.demo.service.IAuthService;
import com.confyrm.demo.service.IEventProducer;
import com.confyrm.demo.service.IEventSender;
import com.confyrm.demo.service.IEventStore;
import com.confyrm.demo.service.impl.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.google.inject.Scopes.SINGLETON;

public class DemoModule extends AbstractModule {

    private final AppConfig appConfig;
    private final EventConfig eventConfig;

    public DemoModule(AppConfig appConfig, EventConfig eventConfig) {

        this.appConfig = appConfig;
        this.eventConfig = eventConfig;
    }

    @Override
    protected void configure() {
        bind(AppConfig.class).toInstance(appConfig);
        bind(EventConfig.class).toInstance(eventConfig);

        bind(Integer.class).annotatedWith(Names.named("rateOfEventsPerSecond")).toInstance(eventConfig.getTotalRate());
        bind(IEventStore.class).to(InMemoryEventStore.class).in(SINGLETON);
        bind(IEventProducer.class).to(EventProducer.class).in(SINGLETON);
        bind(IEventSender.class).to(EventSender.class).in(SINGLETON);
    }

    @Provides
    @Singleton
    public EventGenerator buildEventGenerator() throws IOException {
        List<Identity> identities;
        try (Reader identitiesSource = new FileReader(eventConfig.getIdentitiesFileName())) {
            identities = IdentityDataSource.readIdentities(identitiesSource);
        }
        return new EventGenerator(identities, eventConfig.getRates());
    }

    @Provides
    @Singleton
    public CloseableHttpClient buildHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // trust all certificates (not for production)
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();

        X509HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


        // additional connection option
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10 * 1000)
                .setConnectTimeout(10 * 1000)
                .build();
        return HttpClientBuilder.create()
                .setSslcontext(sslContext)
                .setMaxConnPerRoute(appConfig.getHttpConnections())
                .setMaxConnTotal(appConfig.getHttpConnections())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Provides
    @Singleton
    public IAuthService buildAuthService(CloseableHttpClient httpClient) throws Exception {
        ConfyrmAuthClient confyrmAuthClient = new ConfyrmAuthClient(httpClient, appConfig.getAuthUrl());
        return new AuthService(confyrmAuthClient, appConfig.getLogin(), appConfig.getPassword());
    }

    @Provides
    @Singleton
    public IConfyrmApiClient buildConfyrmApiClient(CloseableHttpClient httpClient) {
        return new ConfyrmApiClient(httpClient, appConfig.getApiUrl());
    }
}
