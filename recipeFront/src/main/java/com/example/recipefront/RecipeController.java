package com.example.recipefront;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("recipe")
public class RecipeController {

    private static final String[] BLACK_LIST = {"DROP", "DELETE", "FROM", "SELECT", "TABLE", "DATABASE", "ALTER", "CREATE", "ADD", ";", "/", ">", "<"};


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
                         @RequestParam("title") String title,
                         @RequestParam("instructions") String instructions,
                         @RequestParam("preparationTime") String preparationTime,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipeLevelOfDifficulty) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredient : recipeIngredients) {
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase()));
        }

        try {
            recipe.setTitle(stringValid(title));
            recipe.setInstructions(stringValid(instructions));
            recipe.setPreparationTime(Long.parseLong(preparationTime));
        } catch (IllegalArgumentException e) {
            Logger logger = LoggerFactory.getLogger(RecipeController.class);
            logger.error("Error validating new recipe data", e);
            return "redirect:/recipe";
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
                         @RequestParam("title") String title,
                         @RequestParam("instructions") String instructions,
                         @RequestParam("preparationTime") Long preparationTime,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipelevelOfDifficulty) {
        recipe.setId(id);
        recipe.setDifficulty(Difficulty.valueOf(recipelevelOfDifficulty.toUpperCase()));
        List<Ingredient> ingredients = new ArrayList<>();
        for ( String ingredient : recipeIngredients){
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase().replaceAll("\\s", "_")));
        }
        try {
            recipe.setTitle(stringValid(title));
            recipe.setInstructions(stringValid(instructions));
        } catch (IllegalArgumentException e) {
            Logger logger = LoggerFactory.getLogger(RecipeController.class);
            logger.error("Error validating recipe update data", e);
            return "redirect:/recipe";
        }
        recipe.setPreparationTime(preparationTime);
        recipe.setIngredients(ingredients);
        recipeService.update(recipe);
        return "redirect:/recipe";
    }

    @GetMapping("delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        recipeService.deleteById(id);
        return "redirect:/recipe";
    }

    public static String stringValid(String input){
        for (String blacklisted : BLACK_LIST) {
            if (input.toUpperCase().contains(blacklisted)) {
                throw new IllegalArgumentException();
            }
        }
        return input;
    }

}