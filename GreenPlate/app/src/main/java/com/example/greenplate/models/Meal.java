package com.example.greenplate.models;


public class Meal {
    private String userId; // To link meal to user
    private String mealName;
    private String calories;

    private String date;

    public Meal() {
        // Default constructor required for Firebase
    }

    public Meal(String mealName, String calories, String userId, String date) {
        this.mealName = mealName;
        this.calories = calories;
        this.userId = userId;
        this.date = date;
    }


    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
