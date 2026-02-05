package com.example.circuit_breaker_demo.service;

import com.example.circuit_breaker_demo.data.DatosValidate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class callExternalApi {

    private final RestTemplate restTemplate;

    public callExternalApi(RestTemplate restTemplate){this.restTemplate = restTemplate;}

    public String validatePreLogin(DatosValidate datosValidate){

        String url = "http://10.160.209.146:9084/CWCREST/services/resources/cobis/cwc/credentials/public/validate";

        HttpEntity<DatosValidate> entity = new HttpEntity<DatosValidate>(datosValidate);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        return response.getBody();
    }
}
