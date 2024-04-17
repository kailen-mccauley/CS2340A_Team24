package com.example.greenplate;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingValidator {
    public static boolean isValidUser(FirebaseUser currentUser) {
        return currentUser != null;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public static boolean isValidIngredient(String ingredient) {
        boolean result = ingredient != null && !ingredient.equals("")
                && ingredient.equals(ingredient.trim());
        if (!result) {
            return false;
        }
        for (int i = 0; i < ingredient.length(); i++) {
            if (ingredient.charAt(i) >= 65 && ingredient.charAt(i) <= 90) {
                return true;
            } else if (ingredient.charAt(i) >= 97 && ingredient.charAt(i) <= 122) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidBuyItemsList(List<String> items) {
        Set<String> set = new HashSet<>(items);
        return set.size() == items.size() && items.size() != 0;
    }
}
