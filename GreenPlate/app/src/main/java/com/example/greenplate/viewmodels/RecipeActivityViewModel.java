package com.example.greenplate.viewmodels;

import com.example.greenplate.sorters.RecipeAlphabeticalSorter;
import com.example.greenplate.sorters.SortRecipe;
import com.example.greenplate.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.example.greenplate.sorters.RecipeUserHasIngredientsSorter;

import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class RecipeActivityViewModel {
    private static volatile RecipeActivityViewModel instance;
    private final Recipe recipeData;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SortRecipe recipeSorter;
    private RecipeActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recipeData = new Recipe();
        recipeSorter = new RecipeUserHasIngredientsSorter();
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

    public void getRecipeDetails(String recipeID, RecipeDetailsListener listener) {
        DatabaseReference recipeRef = mDatabase.child("cookbook").child(recipeID);
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String recipeName = dataSnapshot.child("name").getValue(String.class);
                    Map<String, Integer> ingredientsMap = dataSnapshot.child("ingredients")
                            .getValue(new GenericTypeIndicator<Map<String, Integer>>() { });
                    Recipe recipe = new Recipe(recipeName, ingredientsMap, recipeID);
                    listener.onRecipeDetailsReceived(recipe);
                } else {
                    listener.onRecipeDetailsError("Recipe not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onRecipeDetailsError("Error fetching recipe details: "
                        + error.getMessage());
            }
        });
    }

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
            recipeData.put("recipeID", recipeId);

            if (recipeId != null) {
                mDatabase.child("cookbook").child(recipeId).setValue(recipeData)
                        .addOnSuccessListener(aVoid -> Log.d("storeRecipe",
                                "Recipe successfully stored."))
                        .addOnFailureListener(e -> Log.d("storeRecipe",
                                "Failed to store recipe.", e));
            }
        } else {
            Log.d("storeRecipe", "No authenticated user found.");
        }
    }


    public void fetchRecipesbyIngredAvail(RecipeListListener listener) {
        recipeSorter = new RecipeUserHasIngredientsSorter();
        recipeSorter.sortRecipes(listener);
    }

    public void getRecipeList(RecipeListListener listener) {
        recipeSorter = new RecipeAlphabeticalSorter();
        recipeSorter.sortRecipes(listener);
    }

    public interface RecipeDetailsListener {
        void onRecipeDetailsReceived(Recipe recipe);
        void onRecipeDetailsError(String errorMessage);
    }
    public interface RecipeListListener {
        void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> recipeList);
    }
    public interface IngredientCheckListener {
        void onIngredientCheckResult(boolean hasIngredients);
    }
}