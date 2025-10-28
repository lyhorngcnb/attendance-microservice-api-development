package com.lyhorng.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.lyhorng.common.util.RestClientUtil;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .requestFactory(this::clientHttpRequestFactory)
                .build();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public RestClientUtil restClientUtil(RestTemplate restTemplate) {
        return new RestClientUtil(restTemplate);
    }
    
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(30000);
        return factory;
    }
}