package com.example.greenplate;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingValidator {
    public boolean isValidUser(FirebaseAuth currentUser) {
        return currentUser != null;
    }

    public boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public boolean isValidIngredient(String ingredient) {
        return ingredient != null && !ingredient.equals("");
    }

    public boolean isValidBuyItemsList(List<String> items) {
        Set<String> set = new HashSet<>(items);
        return set.size() == items.size();
    }
}
