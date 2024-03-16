package com.example.greenplate.models;

public class Ingredient {
    private String userId; // To link meal to user
    private String ingredientName;
    private int calories;

    private int quantity;

    public Ingredient() {
        // Default constructor required for Firebase
    }

    public Ingredient(String ingredientName, int calories, int quantity, String userId) {
        this.ingredientName = ingredientName;
        this.calories = calories;
        this.userId = userId;
        this.quantity = quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public int getCalories() {
        return calories;
    }
    public String getUserId() {
        return userId;
    }
    public int getQuantity() {
        return quantity;
    }

}
