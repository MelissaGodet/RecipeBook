package com.example.recipefront.controller;

import com.example.recipefront.model.Difficulty;
import com.example.recipefront.model.Ingredient;
import com.example.recipefront.model.Recipe;
import com.example.recipefront.service.RecipeService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.*;

@Controller
@RequestMapping("recipe")
public class RecipeController {

    private static final String[] SQL_BLACK_LIST = {"DROP ", "DELETE ", "FROM ", "SELECT ", "TABLE ", "DATABASE ", "ALTER ", "CREATE ", ";", "/", ">", "<"};

    private static final Set<Class<?>> DESERIALIZE_WHITELIST = new HashSet<>(Collections.singletonList(Recipe.class));

    private static final String FILE_PATH = "./serialized/";
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
        return "recipe2";
    }

    @PostMapping("new")
    public String create(HttpSession session,
                         @ModelAttribute("recipe") Recipe recipe,
                         @RequestParam("title") String title,
                         @RequestParam("instructions") String instructions,
                         @RequestParam("preparationTime") Long preparationTime,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipeLevelOfDifficulty)
    {
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredient : recipeIngredients) {
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase()));
        }

        try {
            recipe.setTitle(stringValid(title));
            recipe.setInstructions(stringValid(instructions));
            recipe.setPreparationTime(preparationTime);
            recipe.setPreparationTime(preparationTime);
            recipe.setIngredients(ingredients);
            recipe.setDifficulty(Difficulty.valueOf(recipeLevelOfDifficulty.toUpperCase().replaceAll("\\s", "_")));
            String token = (String) session.getAttribute("Authorization");
            recipeService.update(recipe, token);
        } catch (IllegalArgumentException e) {
            Logger logger = LoggerFactory.getLogger(RecipeController.class);
            logger.error("Error validating new recipe data", e);
            return REDIRECT_RECIPEPAGE;
        }
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(Model model, HttpSession session, @PathVariable("id") Long id) {
        String token = (String) session.getAttribute("Authorization");
        Recipe recipe = recipeService.getById(id, token);
        model.addAttribute("recipe", recipe);
        return "recipe2";
    }

    @PostMapping("edit/{id}")
    public String update(HttpSession session,
                         @PathVariable("id") Long id,
                         @ModelAttribute("recipe") Recipe recipe,
                         @RequestParam("title") String title,
                         @RequestParam("instructions") String instructions,
                         @RequestParam("preparationTime") Long preparationTime,
                         @RequestParam("recipeIngredients") List<String> recipeIngredients,
                         @RequestParam("recipeLevelOfDifficulty") String recipeLevelOfDifficulty) {
        recipe.setId(id);
        recipe.setDifficulty(Difficulty.valueOf(recipeLevelOfDifficulty.toUpperCase()));
        List<Ingredient> ingredients = new ArrayList<>();
        for ( String ingredient : recipeIngredients){
            ingredients.add(Ingredient.valueOf(ingredient.toUpperCase().replaceAll("\\s", "_")));
        }
        try {
            recipe.setTitle(stringValid(title));
            recipe.setInstructions(stringValid(instructions));
            recipe.setPreparationTime(preparationTime);
            recipe.setIngredients(ingredients);
            recipe.setDifficulty(Difficulty.valueOf(recipeLevelOfDifficulty.toUpperCase().replaceAll("\\s", "_")));
            String token = (String) session.getAttribute("Authorization");
            recipeService.update(recipe, token);
        } catch (IllegalArgumentException e) {
            Logger logger = LoggerFactory.getLogger(RecipeController.class);
            logger.error("Error validating recipe update data", e);
            return REDIRECT_RECIPEPAGE;
        }
        return REDIRECT_RECIPEPAGE;
    }

    @GetMapping("delete/{id}")
    public String deleteById(HttpSession session, @PathVariable("id") Long id) {
        String token = (String) session.getAttribute("Authorization");
        recipeService.deleteById(id, token);
        return REDIRECT_RECIPEPAGE;
    }

    public static String stringValid(String input){
        for (String blacklisted : SQL_BLACK_LIST) {
            if (input.toUpperCase().contains(blacklisted)) {
                throw new IllegalArgumentException();
            }
        }
        return input;
    }

    @GetMapping("export")
    public ResponseEntity<Resource> export(HttpSession session) {
        String token = (String) session.getAttribute("Authorization");
        String fileName = serializeRecipes(new ArrayList<>(recipeService.getAll(token)));
        Resource resource = new FileSystemResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/import")
    public String importBooks(HttpSession session, @RequestParam("file") MultipartFile file) {
        String token = (String) session.getAttribute("Authorization");
        Logger logger = LoggerFactory.getLogger(RecipeController.class);
        if (file.isEmpty()) {
            logger.warn("Empty file");
            return REDIRECT_RECIPEPAGE;
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path path = Paths.get(fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Recipe> recipes = deserializeRecipes(fileName);
        for(Recipe recipe : recipes) {
            recipe.setId(null);
            recipeService.create(recipe, token);
            logger.info("Recipe \"" + recipe.getTitle() + "\" was successfully imported");
        }

        return REDIRECT_RECIPEPAGE;
    }

    public static String serializeRecipes(ArrayList<Recipe> recipes) {
        String fileName = "recipes.ser";
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(recipes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static List<Recipe> deserializeRecipes(String fileName) {
        List<Recipe> list = new ArrayList<>();

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            while (fileIn.available() > 0) {
                ArrayList<?> arrayList = (ArrayList<?>) in.readObject();

                for (Object element : arrayList) {
                    Class<?> elementClass = element.getClass();

                    if (DESERIALIZE_WHITELIST.contains(elementClass)) {
                        list.add((Recipe) element);
                    } else {
                        throw new SecurityException("Object of non-deserializable class " + elementClass.getSimpleName() + " detected");
                    }
                }
            }

        } catch (SecurityException se) {
            System.out.println("Only recipes can be deserialize");
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}