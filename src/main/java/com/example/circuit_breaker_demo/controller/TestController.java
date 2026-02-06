package com.example.circuit_breaker_demo.controller;

import com.example.circuit_breaker_demo.data.DatosLogin;
import com.example.circuit_breaker_demo.data.DatosValidate;
import com.example.circuit_breaker_demo.service.UnstableService;
import com.example.circuit_breaker_demo.service.callExternalApi;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class TestController {

    private final callExternalApi validate;
    private final callExternalApi login;
    private final callExternalApi laborReferences;

//    private final UnstableService unstableService;

    public TestController(callExternalApi validate, callExternalApi login, callExternalApi laborReferences) {
        //this.unstableService = unstableService;
        this.validate = validate;
        this.login = login;
        this.laborReferences = laborReferences;
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validatePreLogin(@RequestBody DatosValidate datosValidate){
        return ResponseEntity.ok(validate.validatePreLogin(datosValidate));
    }

    @PostMapping("/login")
    public ResponseEntity<String> Login(@RequestBody DatosLogin datosLogin, HttpSession session){
        return ResponseEntity.ok(login.Login(datosLogin, session));
    }

//    @GetMapping("/test")
//    public CompletableFuture<String> test() {
//        return unstableService.callExternalService();
//    }

}
