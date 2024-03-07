package com.example.greenplate;

public class InputValidator {
    public static boolean isValidInput(String input) {
        return input != null && !input.contains(" ") && !input.isEmpty();
    }
}
