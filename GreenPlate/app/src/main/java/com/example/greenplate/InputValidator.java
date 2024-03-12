package com.example.greenplate;

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
}

