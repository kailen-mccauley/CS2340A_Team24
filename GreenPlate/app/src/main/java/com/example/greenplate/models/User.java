package com.example.greenplate.models;

public class User {

    private String userGender;
    private String userWeight;
    private String userHeight;
    private int calorieGoal;

    public User() { }

    public User(String userHeight, String userWeight, String userGender, int calorieGoal) {
        this.setUserGender(userGender);
        this.setUserWeight(userWeight);
        this.setUserHeight(userHeight);
        this.setCalorieGoal(calorieGoal);
    }


    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }
}
