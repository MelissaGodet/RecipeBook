package com.example.recipefront;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public String getAll(Model model) {
        List<Recipe> recipes = recipeService.getAll();
        model.addAttribute("recipes", recipes);
        return "recipe";
    }

    @PostMapping("getById")
    public String postGetById(@Nullable @RequestParam("id") String id) {
        try {
            Long.valueOf(id);
        } catch(NumberFormatException nfe) {
            return "redirect:/recipe";
        }
        return "redirect:/recipe/" + id;
    }

    @GetMapping("{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.getById(id);
        if(recipe==null) {
            return "redirect:/recipe";
        } else {
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(recipe);
            model.addAttribute("recipes", recipes);
            return "recipe";
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
        return "redirect:/recipe";
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
        return "redirect:/recipe";
    }

    @GetMapping("delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        recipeService.deleteById(id);
        return "redirect:/recipe";
    }
}