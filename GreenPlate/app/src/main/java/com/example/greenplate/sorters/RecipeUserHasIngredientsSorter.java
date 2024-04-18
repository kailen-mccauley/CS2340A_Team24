package com.example.greenplate.sorters;

import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeUserHasIngredientsSorter implements SortRecipe {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public RecipeUserHasIngredientsSorter() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private ArrayList<Map<SpannableString, Recipe>> convertToExpectedFormat(List<Recipe>
                                                                                    sortedRecipes) {
        ArrayList<Map<SpannableString, Recipe>> formattedList = new ArrayList<>();
        for (Recipe recipe : sortedRecipes) {
            SpannableString recipeNameSpannable = new SpannableString(recipe.getRecipeName());
            Map<SpannableString, Recipe> recipeMap = new HashMap<>();
            recipeMap.put(recipeNameSpannable, recipe);
            formattedList.add(recipeMap);
        }
        return formattedList;
    }

    public void sortRecipes(RecipeActivityViewModel.RecipeListListener listener) {
        ArrayList<Recipe> sortedRecipes = new ArrayList<>();
        DatabaseReference cookbookRef = mDatabase.child("cookbook");
        cookbookRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Recipe> recipesWithIngredients = new ArrayList<>();
                List<Recipe> recipesWithoutIngredients = new ArrayList<>();

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        Log.d("FetchData", "Recipe fetched: " + recipe.getRecipeName());
                    } else {
                        Log.d("FetchData", "Failed to parse a Recipe from snapshot: "
                                + recipeSnapshot.getValue());
                    }
                    if (recipe != null) {
                        doesUserHaveIngredients(recipe, hasIngredients -> {
                            if (hasIngredients) {
                                recipesWithIngredients.add(recipe);
                            } else {
                                recipesWithoutIngredients.add(recipe);
                            }

                            if (recipesWithIngredients.size() + recipesWithoutIngredients.size()
                                    == dataSnapshot.getChildrenCount()) {
                                sortedRecipes.addAll(recipesWithIngredients);
                                ArrayList<Map<SpannableString, Recipe>> finalList
                                        = convertToExpectedFormat(sortedRecipes);
                                listener.onRecipeListReceived(finalList);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseError", "Error fetching recipes.");
            }
        });
    }

    public void doesUserHaveIngredients(Recipe recipe,
                                        RecipeActivityViewModel.IngredientCheckListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference pantryRef = mDatabase.child("pantry");

            pantryRef.orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Integer> recipeIngredients = recipe.getIngredients();
                            boolean hasAllIngredients = true;

                            // Here we store a count of each ingredient found
                            Map<String, Integer> pantryQuantities = new HashMap<>();
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                String pantryIngredientName = itemSnapshot.child("ingredientName")
                                        .getValue(String.class);
                                Integer pantryQuantity = itemSnapshot.child("quantity")
                                        .getValue(Integer.class);

                                if (pantryIngredientName != null && pantryQuantity != null) {
                                    pantryQuantities.put(pantryIngredientName, pantryQuantities
                                            .getOrDefault(pantryIngredientName, 0)
                                            + pantryQuantity);
                                }
                            }

                            // Now check if we have enough of each ingredient
                            for (Map.Entry<String, Integer> entry : recipeIngredients.entrySet()) {
                                String ingredientName = entry.getKey();
                                int requiredQuantity = entry.getValue();

                                Integer pantryQuantity = pantryQuantities.get(ingredientName);
                                if (pantryQuantity == null || pantryQuantity < requiredQuantity) {
                                    hasAllIngredients = false;
                                    break;
                                }
                            }

                            listener.onIngredientCheckResult(hasAllIngredients);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            listener.onIngredientCheckResult(false);
                        }
                    });
        } else {
            listener.onIngredientCheckResult(false);
        }
    }
}
