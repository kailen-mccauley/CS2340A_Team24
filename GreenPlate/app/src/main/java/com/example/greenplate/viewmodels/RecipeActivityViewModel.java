package com.example.greenplate.viewmodels;

import com.example.greenplate.RecipeAlphabeticalSorter;
import com.example.greenplate.SortRecipe;
import com.example.greenplate.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.example.greenplate.RecipeUserHasIngredientsSorter;

import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

public class RecipeActivityViewModel {
    private static volatile RecipeActivityViewModel instance;
    private final Recipe recipeData;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SortRecipe recipeSorter;

    private SortRecipe sortRecipeInstance;
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

    public void doesUserHaveIngredients(Recipe recipe, IngredientCheckListener listener) {
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

//    public void getRecipeList(RecipeListListener listener) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        ArrayList<Map<SpannableString, Recipe>> recipeList = new ArrayList<>();
//
//        if (currentUser != null) {
//            String uid = currentUser.getUid();
//            mDatabase.child("cookbook").orderByChild("name")
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                                String recipeName = snapshot.child("name").getValue(String.class);
//                                SpannableString name = new SpannableString(recipeName);
//                                Map<String, Integer> ingredientsMap = (Map<String, Integer>)
//                                        snapshot.child("ingredients").getValue();
//                                String recipeID = snapshot.child("recipeID").getValue(String.class);
//                                Recipe recipe = new Recipe(recipeName, ingredientsMap, recipeID);
//                                Map<SpannableString, Recipe> recipeMap = new HashMap<>();
//                                recipeMap.put(name, recipe);
//                                recipeList.add(recipeMap);
//                            }
//
//                            listener.onRecipeListReceived(recipeList);
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//        }
//    }


    public SortRecipe getSortRecipeInstance() {
        return sortRecipeInstance;
    }

    public void setSortRecipeInstance(SortRecipe sortRecipeInstance) {
        this.sortRecipeInstance = sortRecipeInstance;
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