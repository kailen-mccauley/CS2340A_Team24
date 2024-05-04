package com.example.greenplate.utilites;
import android.content.Intent;

import com.example.greenplate.models.User;

public class InputValidator {
    public static boolean isValidInput(String input) {
        return input != null && !input.contains(" ") && !input.isEmpty();
    }
    public static boolean isValidInputWithSpacesBetween(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.equals(input.trim());
    }

    public static boolean isValidInputWithInteger(String input) {
        try {
            int num = Integer.parseInt(input);
            if (num < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return isValidInput(input);
    }

    public static boolean isValidGender(String gender) {
        return gender != null && ((gender.equals("M")) || (gender.equals("F")));
    }

    public static boolean isValidSpinnerItem(String spinnerItem) {
        return !spinnerItem.contains("Select");
    }
    public static boolean isValidQuantityDecrease(int decreaseQuantity, int currentQuantity) {
        return decreaseQuantity <= currentQuantity;
    }

    public static boolean isValidRecipeDetailIntent(Intent intent) {
        if (intent != null && intent.hasExtra("recipeID") && intent.hasExtra("recipeName")) {
            return true;
        }
        return false;
    }
    public static boolean isValidTime(String timerOutput) {
        return !timerOutput.equals("00:00:00");
    }

    public static boolean isValidUserInfo(User user) {
        return user.getUserGender() != null
                && user.getUserWeight() != null
                && user.getUserHeight() != null
                && user.getCalorieGoal() != 0
                && user.getStepGoal() != 0
                && user.getWorkoutGoalInMinutes() != 0;
    }


}

