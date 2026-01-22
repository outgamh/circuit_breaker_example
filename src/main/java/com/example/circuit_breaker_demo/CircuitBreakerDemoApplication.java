package com.example.circuit_breaker_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CircuitBreakerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerDemoApplication.class, args);
	}

}
