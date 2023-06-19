package com.example.recipefront.controller;

import com.example.recipefront.model.Difficulty;
import com.example.recipefront.model.Ingredient;
import com.example.recipefront.model.Recipe;
import com.example.recipefront.service.RecipeService;
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
    public String getAll(Model model) {
        List<Recipe> recipes = recipeService.getAll();
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
    public String getById(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getById(id);
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
    public String create(@ModelAttribute("recipe") Recipe recipe,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipeLevelOfDifficulty)
    {
        List<Ingredient> ingredients = new ArrayList<>();
        for ( String ingredient : recipeIngredients){
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase()));
        }
        recipe.setIngredients(ingredients);
        recipe.setDifficulty(Difficulty.valueOf(recipeLevelOfDifficulty.toUpperCase().replaceAll("\\s", "_")));
        recipeService.create(recipe);
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("redirectionUrl","/recipe/edit/"+id);
        return "recipe2";
    }

    @PostMapping("edit/{id}")
    public String update(@PathVariable("id") Long id,
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
        recipeService.update(recipe);
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        recipeService.deleteById(id);
        return REDIRECT_RECIPEPAGE;
    }
}