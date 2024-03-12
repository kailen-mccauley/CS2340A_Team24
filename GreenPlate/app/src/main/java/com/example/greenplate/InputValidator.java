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
            Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return isValidInput(input);
    }
}
