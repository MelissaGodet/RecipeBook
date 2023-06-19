package com.example.recipefront.controller;

import com.example.recipefront.model.Difficulty;
import com.example.recipefront.model.Ingredient;
import com.example.recipefront.model.Jwt;
import com.example.recipefront.model.Recipe;
import com.example.recipefront.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recipe")
public class RecipeController {

    private final static String REDIRECT_RECIPEPAGE = "redirect:/recipe";
    private final static String RECIPEPAGE = "recipe";

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        String token = (String) session.getAttribute("Authorization");
        List<Recipe> recipes = recipeService.getAll(token);
        model.addAttribute("recipes", recipes);
        return RECIPEPAGE;
    }

    @PostMapping("getById")
    public String postGetById(@Nullable @RequestParam("id") String id) {
        try {
            Long.valueOf(id);
        } catch(NumberFormatException nfe) {
            return REDIRECT_RECIPEPAGE;
        }
        return REDIRECT_RECIPEPAGE + "/" +id;
    }

    @GetMapping("{id}")
    public String getById(Model model, HttpSession session, @PathVariable("id") Long id) {
        String token = (String) session.getAttribute("Authorization");
        Recipe recipe = recipeService.getById(id, token);
        if(recipe==null) {
            return REDIRECT_RECIPEPAGE;
        } else {
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(recipe);
            model.addAttribute("recipes", recipes);
            return RECIPEPAGE;
        }
    }

    @GetMapping("new")
    public String showCreateForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("redirectionUrl","/recipe/new");
        return "recipe2";
    }

    @PostMapping("new")
    public String create(HttpSession session,
                         @ModelAttribute("recipe") Recipe recipe,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipeLevelOfDifficulty)
    {
        List<Ingredient> ingredients = new ArrayList<>();
        for ( String ingredient : recipeIngredients){
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase()));
        }
        recipe.setIngredients(ingredients);
        recipe.setDifficulty(Difficulty.valueOf(recipeLevelOfDifficulty.toUpperCase().replaceAll("\\s", "_")));
        String token = (String) session.getAttribute("Authorization");
        recipeService.create(recipe, token);
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(Model model, HttpSession session, @PathVariable("id") Long id) {
        String token = (String) session.getAttribute("Authorization");
        Recipe recipe = recipeService.getById(id, token);
        model.addAttribute("recipe", recipe);
        model.addAttribute("redirectionUrl","/recipe/edit/"+id);
        return "recipe2";
    }

    @PostMapping("edit/{id}")
    public String update(HttpSession session,
                         @PathVariable("id") Long id,
                         @ModelAttribute("recipe") Recipe recipe,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipelevelOfDifficulty) {
        recipe.setId(id);
        recipe.setDifficulty(Difficulty.valueOf(recipelevelOfDifficulty.toUpperCase()));
        List<Ingredient> ingredients = new ArrayList<>();
        for ( String ingredient : recipeIngredients){
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase().replaceAll("\\s", "_")));
        }
        recipe.setIngredients(ingredients);
        String token = (String) session.getAttribute("Authorization");
        recipeService.update(recipe, token);
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("delete/{id}")
    public String deleteById(HttpSession session, @PathVariable("id") Long id) {
        String token = (String) session.getAttribute("Authorization");
        recipeService.deleteById(id, token);
        return REDIRECT_RECIPEPAGE;
    }
}