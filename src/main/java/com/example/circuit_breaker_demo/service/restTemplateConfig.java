package com.example.circuit_breaker_demo.service;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.LaxRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class restTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        CookieStore cookieStore = new BasicCookieStore();

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setRedirectStrategy(new LaxRedirectStrategy()).build();

        org.springframework.http.client.HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return  new RestTemplate(factory);
    }
}
