package com.example.greenplate.models;

import java.util.Map;
import com.google.firebase.database.PropertyName;

public class Recipe {
    private String name;
    private Map<String, Integer> ingredients;
    private String recipeID;

    public Recipe() {
        // Default constructor required for Firebase
    }

    public Recipe(String name, Map<String, Integer> ingredients, String recipeID) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipeID = recipeID;
    }

    @PropertyName("name")
    public String getRecipeName() {
        return name;
    }

    @PropertyName("name")
    public void setRecipeName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
}
