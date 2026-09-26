package com.highonline.exampleapi.infrastructure.adapters.out.web.translator.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
class TranslatorAPIConfig {

    @Bean
    RestClient translatorRestClient(RestClient.Builder builder,
                                    @Value("${example-api.translator.url}") String url,
                                    @Value("${example-api.translator.connect-timeout}") Duration connectTimeout,
                                    @Value("${example-api.translator.read-timeout}") Duration readTimeout) {
        var factory = new JdkClientHttpRequestFactory(HttpClient.newBuilder().connectTimeout(connectTimeout).build());
        factory.setReadTimeout(readTimeout);
        return builder.baseUrl(url).requestFactory(factory).build();
    }
}
