package hr.algebra.aisi_project.model;

import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name="recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    @ElementCollection(targetClass = Ingredient.class)
    @CollectionTable(name = "recipeIngredient", joinColumns = @JoinColumn(name = "recipeId"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ingredient")
    List<Ingredient> ingredients;

    String instructions;

    Long preparationTime;
    @Enumerated(value =EnumType.STRING)
    Difficulty difficulty;

    public Recipe(Long id, String title, List<Ingredient> ingredients, String instructions, Long preparationTime, Difficulty difficulty) {
        this.id = id;
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
}
