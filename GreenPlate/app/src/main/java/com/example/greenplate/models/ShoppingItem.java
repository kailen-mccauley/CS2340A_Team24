package com.example.greenplate.models;

public class ShoppingItem {
    private String userId; // To link shopping item to user
    private String ingredientName;

    private int quantity;
    private int calories;

    public ShoppingItem() {
        // Default constructor required for Firebase
    }

    public ShoppingItem(String ingredientName, int quantity, String userId, int calories) {
        this.ingredientName = ingredientName;
        this.userId = userId;
        this.quantity = quantity;
        this.calories = calories;
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public String getUserId() {
        return userId;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getCalories() {
        return calories;
    }
}
