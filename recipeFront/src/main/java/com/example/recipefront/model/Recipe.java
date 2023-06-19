package com.example.recipefront.model;

import java.util.List;
import java.util.Objects;

public class Recipe {

    Long id;

    String title;

    List<Ingredient> ingredients;

    String instructions;

    Long preparationTime;

    Difficulty difficulty;

    public Recipe(String title, List<Ingredient> ingredients, String instructions, Long preparationTime, Difficulty difficulty) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.difficulty = difficulty;
    }

    public Recipe() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Long getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Long preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return this.id == recipe.getId() && Objects.equals(this.ingredients, recipe.getIngredients()) && Objects.equals(this.instructions, recipe.getInstructions()) && this.preparationTime == recipe.getPreparationTime() && Objects.equals(this.difficulty, recipe.getDifficulty()) && Objects.equals(this.title, recipe.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, ingredients, instructions, preparationTime, difficulty);
    }

    @Override
    public String toString() {
        return title + " - " + ingredients + " - " + instructions + " - " + preparationTime + " - " +  difficulty;
    }

}
