package com.example.recipefront.service;


import com.example.recipefront.model.Recipe;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeService {

    private static final String URL = "http://10.42.0.1:8081/recipe";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public Recipe create(Recipe newRecipe) {
        HttpEntity<Recipe> request = new HttpEntity<>(newRecipe);
        return REST_TEMPLATE.exchange(URL, HttpMethod.POST, request, Recipe.class).getBody();
    }

    public Recipe getById(Long id) {
        return REST_TEMPLATE.getForObject(URL+"/"+id, Recipe.class);
    }

    public List<Recipe> getAll() {
        ResponseEntity<Recipe[]> response = REST_TEMPLATE.getForEntity(URL, Recipe[].class);
        return List.of(Objects.requireNonNull(response.getBody()));
    }

    public void update(Recipe updatedRecipe) {
        HttpEntity<Recipe> request = new HttpEntity<>(updatedRecipe);
        ResponseEntity<Recipe> response = REST_TEMPLATE.exchange(URL, HttpMethod.PUT, request, Recipe.class);
    }

    public void deleteById(Long id) {
        REST_TEMPLATE.delete(URL+"/"+id);
    }



}