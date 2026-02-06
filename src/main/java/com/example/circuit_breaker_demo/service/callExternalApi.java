package com.example.circuit_breaker_demo.service;

import com.example.circuit_breaker_demo.data.DatosLogin;
import com.example.circuit_breaker_demo.data.DatosValidate;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public String Login(DatosLogin datosLogin, HttpSession session){

        String url = "http://10.160.209.146:9084/CWCREST/services/resources/cobis/cwc/authentication/public/login";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<DatosLogin> entity = new HttpEntity<DatosLogin>(datosLogin);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        response.getHeaders().forEach((String k, List<String> v) ->System.out.println("HEADER " + k + "=" + v));

        String authorizationHeader = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null){
            authorizationHeader = response.getHeaders().getFirst("X-Authorization");
        }

        if (authorizationHeader == null)
        {
            authorizationHeader = response.getHeaders().getFirst("X-Auth-Token");
        }

        if(authorizationHeader == null)
        {
            throw new RuntimeException("Sin exposicion de token");
        }

        session.setAttribute("TOKEN", authorizationHeader);

        return authorizationHeader;
    }
}
