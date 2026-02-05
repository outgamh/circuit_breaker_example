package com.example.circuit_breaker_demo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UnstableService {
    private static final String CB_NAME = "backendService";


    public String fallback(Throwable t){
        System.out.println("Fallback ejecutado: " + t.getMessage());
        return "Respuesta desde el fallback";
    }
}
