package com.example.recipefront.service;
import com.example.recipefront.model.Jwt;
import com.example.recipefront.model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthentificationService {

    private static final String ROOT_URL = "http://localhost:8081/authentication/login";

    public static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public Jwt send(Login login){
        HttpEntity<Login> request = new HttpEntity<>(login);
        ResponseEntity<Jwt> response = REST_TEMPLATE.exchange(ROOT_URL, HttpMethod.POST, request, Jwt.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        response.getBody();
        return response.getBody();
    }

}
