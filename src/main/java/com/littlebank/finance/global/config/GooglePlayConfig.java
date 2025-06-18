package com.littlebank.finance.global.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.auth.http.HttpCredentialsAdapter;
import groovy.util.logging.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.InputStream;
import java.util.Collections;

@Slf4j
@Configuration
public class GooglePlayConfig {

    @Bean
    public GoogleCredentials googleCredentials() throws Exception {
        InputStream inputStream = new ClassPathResource("google-play/google-play-account.json").getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

        credentials.refreshIfExpired(); // access token 갱신
        return credentials;
    }

    @Bean
    public AndroidPublisher androidPublisher(GoogleCredentials credentials) throws Exception {
        return new AndroidPublisher.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("littlebank").build();
    }
}
