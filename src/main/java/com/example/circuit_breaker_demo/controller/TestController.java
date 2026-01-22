package com.example.circuit_breaker_demo.controller;

import com.example.circuit_breaker_demo.service.UnstableService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class TestController {

//    private final UnstableService unstableService;
//
//    public TestController(UnstableService unstableService) {
//        this.unstableService = unstableService;
//    }
//
//    @GetMapping("/test")
//    public String test() {
//        return unstableService.callExternalService();
//    }

    private final UnstableService unstableService;

    public TestController(UnstableService unstableService) {
        this.unstableService = unstableService;
    }

    @GetMapping("/test")
    public CompletableFuture<String> test() {
        return unstableService.callExternalService();
    }
}
