package com.example.circuit_breaker_demo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UnstableService {
    private static final String CB_NAME = "backendService";
    private final Random random = new Random();

    @CircuitBreaker(name = CB_NAME /*fallbackMethod = "fallback"*/)
    public String callExternalService() {

        // Simulamos fallos aleatorios
        if (random.nextBoolean()) {
            System.out.println("❌ Falló el servicio externo");
            throw new RuntimeException("Servicio externo caído");
        }
        System.out.println("✅ Servicio externo OK");
        return "Respuesta OK del servicio externo";


        //throw new RuntimeException("Servicio externo caído");

    }

    // Fallback
    public String fallback(Throwable t) {
        System.out.println("⚠️ Fallback ejecutado: " + t.getMessage());
        return "Respuesta desde fallback";
    }
}
