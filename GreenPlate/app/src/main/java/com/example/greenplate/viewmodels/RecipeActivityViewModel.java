package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Meal;
import com.example.greenplate.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecipeActivityViewModel {
    private static volatile RecipeActivityViewModel instance;
    private final Recipe recipeData;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecipeActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recipeData = new Recipe();
    }

    public static RecipeActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (RecipeActivityViewModel.class) {
                if (instance == null) {
                    instance = new RecipeActivityViewModel();
                }
            }
        }
        return instance;
    }

    //TODO: storeRecipe method
    // Should be a map of ingredient,quantity pairs
    // Each recipe should be visible to all other users
    public void storeRecipe(String recipeName, HashMap<String, Integer> ingredientsMap) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Generate a unique key for the new recipe
            String recipeId = mDatabase.child("recipes").push().getKey();

            // Create a map to store both the name and the ingredients of the recipe
            Map<String, Object> recipeData = new HashMap<>();
            recipeData.put("name", recipeName);
            recipeData.put("ingredients", ingredientsMap);

            if (recipeId != null) {
                mDatabase.child("cookbook").child(recipeId).setValue(recipeData)
                        .addOnSuccessListener(aVoid -> Log.d("storeRecipe", "Recipe successfully stored."))
                        .addOnFailureListener(e -> Log.d("storeRecipe", "Failed to store recipe.", e));
            }
        } else {
            Log.d("storeRecipe", "No authenticated user found.");
        }
    }

    // TODO: doesUserHaveIngredients method
    //  checks if a user has enough ingredients in their pantry to make a recipe
    public boolean doesUserHaveIngredients(Recipe recipe) {
        FirebaseUser currUser = mAuth.getCurrentUser();
        boolean[] res = {true};
        if (currUser != null) {
            String uid = currUser.getUid();
            mDatabase.child("pantry").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Ingredient ingredient = snapshot1.getValue(Ingredient.class);
                                if (ingredient != null && recipe.getIngredients().containsKey(ingredient.getIngredientName())) {
                                    Integer recipeQuantity = recipe.getIngredients().get(ingredient.getIngredientName());
                                    if (recipeQuantity != null) {
                                        int pantryQuantity = ingredient.getQuantity();
                                        if (pantryQuantity < recipeQuantity) {
                                            res[0] = false;
                                            break;
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("doesUserHaveIngredients", "Error accessing database.");
                        }
                    });
        } else {
            Log.d("doesUserHaveIngredients", "No authenticated user found.");
        }
        return res[0];
    }

    //TODO: withUserIngredients
    // creates a list of recipe objects that a user can make.


    //TODO: sortRecipes
    // sort recipes in alphabetical order

}
