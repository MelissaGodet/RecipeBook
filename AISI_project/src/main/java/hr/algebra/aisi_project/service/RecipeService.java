package hr.algebra.aisi_project.service;

import hr.algebra.aisi_project.model.Recipe;
import hr.algebra.aisi_project.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService{
    @Autowired
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository RecipeRepository) {
        this.recipeRepository = RecipeRepository;
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll().stream().toList();
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository
                .save(recipe);
    }

    public Recipe update(Recipe recipe) {
        return recipeRepository
                .save(recipe);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
    
}
