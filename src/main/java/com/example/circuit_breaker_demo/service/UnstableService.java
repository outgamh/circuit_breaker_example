package com.example.circuit_breaker_demo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UnstableService {
    private static final String CB_NAME = "backendService";
    private final WebClient webClient;

    public UnstableService(WebClient webClient){

        this.webClient = webClient;
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallback")
    public String callExternalService(){
        return webClient
                .get()
                .uri("http://10.160.209.146:9084/CWCREST/services/resources/cobis/api/ref_laboral/customers/48/labor-references")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String fallback(Throwable t){
        System.out.println("Fallback ejecutado: " + t.getMessage());
        return "Respuesta desde el fallback";
    }
}
