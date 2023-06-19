package com.example.recipefront.service;


import com.example.recipefront.model.Recipe;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeService {

    private static final String URL = "http://localhost:8081/recipe";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public Recipe create(Recipe newRecipe, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<Recipe> request = new HttpEntity<>(newRecipe, httpHeaders);
        return REST_TEMPLATE.exchange(URL, HttpMethod.POST, request, Recipe.class).getBody();
    }

    public Recipe getById(Long id, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<Recipe> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Recipe> response = REST_TEMPLATE.exchange(URL+"/"+id, HttpMethod.GET, request, Recipe.class);
        return response.getBody();
    }

    public List<Recipe> getAll(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Recipe[]> response = REST_TEMPLATE.exchange(URL, HttpMethod.GET, request, Recipe[].class);
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    public void update(Recipe updatedRecipe, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<Recipe> request = new HttpEntity<>(updatedRecipe, httpHeaders);
        ResponseEntity<Recipe> response = REST_TEMPLATE.exchange(URL, HttpMethod.PUT, request, Recipe.class);
    }

    public void deleteById(Long id, String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Recipe[]> response = REST_TEMPLATE.exchange(URL+"/"+id, HttpMethod.DELETE, request, Recipe[].class);
    }



}