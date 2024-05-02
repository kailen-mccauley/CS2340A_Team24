package com.example.greenplate.models;

import java.time.Duration;

public class User {

    private String userGender;
    private String userWeight;
    private String userHeight;
    private int calorieGoal;
    private int stepGoal;
    private int workoutGoalInMinutes;

    public User() { }

    public User(String userHeight, String userWeight, String userGender,
                int calorieGoal, int stepGoal, int workoutGoal) {
        this.setUserGender(userGender);
        this.setUserWeight(userWeight);
        this.setUserHeight(userHeight);
        this.setCalorieGoal(calorieGoal);
        this.setStepGoal(stepGoal);
        this.setWorkoutGoalInMinutes(workoutGoal);
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
    public int getStepGoal() {
        return stepGoal;
    }
    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }
    public int getWorkoutGoalInMinutes() {
        return workoutGoalInMinutes;
    }
    public void setWorkoutGoalInMinutes(int workoutGoal) {
        this.workoutGoalInMinutes = workoutGoal;
    }
}
