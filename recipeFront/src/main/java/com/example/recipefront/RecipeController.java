package com.example.recipefront;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLOutput;
import java.util.*;

@Controller
@RequestMapping("recipe")
public class RecipeController {

    private static final String[] SQL_BLACK_LIST = {"DROP", "DELETE", "FROM", "SELECT", "TABLE", "DATABASE", "ALTER", "CREATE", "ADD", ";", "/", ">", "<"};

    private static final Set<Class<?>> DESERIALIZE_WHITELIST = new HashSet<>(Collections.singletonList(Recipe.class));

    private static final String FILE_PATH = "./serialized/";

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
        for (String blacklisted : SQL_BLACK_LIST) {
            if (input.toUpperCase().contains(blacklisted)) {
                throw new IllegalArgumentException();
            }
        }
        return input;
    }

    @GetMapping("export")
    public ResponseEntity<Resource> export() {
        String fileName = serializeRecipes(new ArrayList<>(recipeService.getAll()));
        Resource resource = new FileSystemResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/import")
    public String importBooks(@RequestParam("file") MultipartFile file) {
        Logger logger = LoggerFactory.getLogger(RecipeController.class);
        if (file.isEmpty()) {
            logger.warn("Empty file");
            return "redirect:/recipe";
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
            recipeService.create(recipe);
            logger.info("Recipe \"" + recipe.getTitle() + "\" was successfully imported");
        }

        return "redirect:/book";
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