package com.example.greenplate.models;

import java.util.Map;
import com.google.firebase.database.PropertyName;

public class Recipe {
    private String name;
    private Map<String, Integer> ingredients;

    public Recipe() {
        // Default constructor required for Firebase
    }

    public Recipe(String name, Map<String, Integer> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
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
}
