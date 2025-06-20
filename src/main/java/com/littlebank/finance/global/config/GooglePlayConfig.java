package com.littlebank.finance.global.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.auth.http.HttpCredentialsAdapter;
import groovy.util.logging.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class GooglePlayConfig {


    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        Resource resource = new ClassPathResource("google-play/google-play-account.json");
        return GoogleCredentials.fromStream(resource.getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/androidpublisher"));
    }

    @Bean
    public AndroidPublisher androidPublisher(GoogleCredentials credentials)
            throws GeneralSecurityException, IOException {
        return new AndroidPublisher.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("littlebank").build();
    }
}