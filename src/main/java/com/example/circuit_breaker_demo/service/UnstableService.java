package com.example.circuit_breaker_demo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UnstableService {
//    private static final String CB_NAME = "backendService";
//    private final Random random = new Random();
//
//    @CircuitBreaker(name = CB_NAME /*fallbackMethod = "fallback"*/)
//    public String callExternalService() {
//
//        // Simulamos fallos aleatorios
//        if (random.nextBoolean()) {
//            System.out.println("❌ Falló el servicio externo");
//            throw new RuntimeException("Servicio externo caído");
//        }
//        System.out.println("✅ Servicio externo OK");
//        return "Respuesta OK del servicio externo";
//    }
//
//    // Fallback
//    public String fallback(Throwable t) {
//        System.out.println("⚠️ Fallback ejecutado: " + t.getMessage());
//        return "Respuesta desde fallback";
//    }
private static final String NAME = "backendService";
    private final AtomicInteger counter = new AtomicInteger(0);

    @CircuitBreaker(name = NAME, fallbackMethod = "fallback")
    @Retry(name = NAME)
    @TimeLimiter(name = NAME)
    public CompletableFuture<String> callExternalService() {

        return CompletableFuture.supplyAsync(() -> {

            int attempt = counter.incrementAndGet();
            System.out.println("➡️ Llamada #" + attempt);

            // Simulamos latencia
            try {
                Thread.sleep(3000); // 3s > timeout (2s)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Fallos determinísticos
            if (attempt <= 3) {
                System.out.println("❌ Fallo forzado");
                throw new RuntimeException("Servicio externo caído");
            }

            System.out.println("✅ Servicio externo OK");
            return "Respuesta OK en intento " + attempt;
        });
    }

    // Fallback obligatorio con misma firma + Throwable
    public CompletableFuture<String> fallback(Throwable t) {
        System.out.println("⚠️ Fallback ejecutado: " + t.getMessage());
        return CompletableFuture.completedFuture("Respuesta desde fallback");
    }
}
