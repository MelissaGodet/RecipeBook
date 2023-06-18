package hr.algebra.aisi_project.controller;

import hr.algebra.aisi_project.service.*;
import hr.algebra.aisi_project.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("recipe")
public class RecipeController {

    @Autowired
    private final RecipeService recipeService;


    public RecipeController(RecipeService RecipeService) {
        this.recipeService = RecipeService;
    }

    @GetMapping
    public List<Recipe> getAll() {
        return recipeService.findAll();
    }

    @GetMapping("{id}")
    public Recipe getById(@PathVariable final Long id) {
        return recipeService.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe was not found by that id")
                );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Recipe save( @RequestBody final Recipe recipe){
        return recipeService.save(recipe);
    }

    @PutMapping
    public Recipe update(@RequestBody final Recipe recipe){
        return recipeService.update(recipe);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        recipeService.deleteById(id);
    }

}