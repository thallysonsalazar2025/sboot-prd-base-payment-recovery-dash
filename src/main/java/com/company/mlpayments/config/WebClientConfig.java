package com.company.mlpayments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient webClient(@Value("${mercadopago.base-url}") String baseUrl, @Value("${mercadopago.token:}") String token){
        return WebClient.builder().baseUrl(baseUrl).defaultHeader("Authorization", "Bearer "+token).build();
    }
}
