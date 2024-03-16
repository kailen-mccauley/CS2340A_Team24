package com.example.greenplate.models;
import java.util.Map;

public class Recipe {
    private String recipeName;
    private Map<String, Integer> ingredients;

    public Recipe() {
        // Default constructor required for Firebase
    }

    public Recipe(String recipeName,Map<String, Integer> ingredients) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public String getRecipeName() {
        return recipeName;
    }


    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

}
