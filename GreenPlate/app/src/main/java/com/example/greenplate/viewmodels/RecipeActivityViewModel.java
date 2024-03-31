package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Meal;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.sortRecipe;
import com.example.greenplate.sortRecipeAlphabetical;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class RecipeActivityViewModel {
    private static volatile RecipeActivityViewModel instance;
    private final Recipe recipeData;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    private sortRecipe sortRecipeInstance;
    private RecipeActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recipeData = new Recipe();
        sortRecipeInstance = new sortRecipeAlphabetical();
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
            recipeData.put("recipeID", recipeId);

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
//    public boolean doesUserHaveIngredients(Recipe recipe) {
//        FirebaseUser currUser = mAuth.getCurrentUser();
//        boolean[] res = {true};
//        if (currUser != null) {
//            String uid = currUser.getUid();
//            mDatabase.child("pantry").equalTo(uid)
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                Ingredient ingredient = snapshot1.getValue(Ingredient.class);
//                                if (ingredient != null && recipe.getIngredients().containsKey(ingredient.getIngredientName())) {
//                                    Integer recipeQuantity = recipe.getIngredients().get(ingredient.getIngredientName());
//                                    if (recipeQuantity != null) {
//                                        int pantryQuantity = ingredient.getQuantity();
//                                        if (pantryQuantity < recipeQuantity) {
//                                            res[0] = false;
//                                            break;
//                                        }
//                                    }
//                                }
//
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.d("doesUserHaveIngredients", "Error accessing database.");
//                        }
//                    });
//        } else {
//            Log.d("doesUserHaveIngredients", "No authenticated user found.");
//        }
//        return res[0];
//    }

    public interface IngredientCheckListener {
        void onIngredientCheckResult(boolean hasIngredients);
    }

    public void doesUserHaveIngredients(Recipe recipe, IngredientCheckListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference pantryRef = mDatabase.child("pantry");

            pantryRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String, Integer> recipeIngredients = recipe.getIngredients();
                    boolean hasAllIngredients = true;

                    // Here we store a count of each ingredient found
                    Map<String, Integer> pantryQuantities = new HashMap<>();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String pantryIngredientName = itemSnapshot.child("ingredientName").getValue(String.class);
                        Integer pantryQuantity = itemSnapshot.child("quantity").getValue(Integer.class);

                        if (pantryIngredientName != null && pantryQuantity != null) {
                            pantryQuantities.put(pantryIngredientName, pantryQuantities.getOrDefault(pantryIngredientName, 0) + pantryQuantity);
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
                    Log.d("FirebaseData", "Error checking ingredients: " + error.getMessage());
                    listener.onIngredientCheckResult(false);
                }
            });
        } else {
            listener.onIngredientCheckResult(false);
        }
    }



    //TODO: sortRecipes
    // sort recipes in alphabetical order
    public void sortRecipes() {
        DatabaseReference cookbookRef = mDatabase.child("cookbook");
        cookbookRef.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }

                //Logged for now but cassandra going to use it for her UI
                for (Recipe r : recipes) {
                    Log.d("SortedRecipe", "Recipe Name: " + r.getRecipeName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseError", "loadRecipe:onCancelled", error.toException());
            }
        });
    }

    public interface RecipeListListener {
        void onRecipeListReceived(ArrayList<Map<SpannableString,Recipe>> recipeList);
    }
    public void getRecipelist(RecipeListListener Listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ArrayList<Map<SpannableString,Recipe>> recipeList = new ArrayList<>();

        if (currentUser!= null) {
            String uid = currentUser.getUid();
            mDatabase.child("cookbook").orderByChild("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                String recipeName = snapshot.child("name").getValue(String.class);
                                SpannableString name = new SpannableString(recipeName);
                                Map<String, Integer> ingredientsMap = (Map<String, Integer>) snapshot.child("ingredients").getValue();
                                String recipeID = snapshot.child("recipeID").getValue(String.class);
                                Recipe recipe = new Recipe(recipeName, ingredientsMap, recipeID);
                                Map<SpannableString,Recipe> recipeMap = new HashMap<>();
                                recipeMap.put(name, recipe);
                                recipeList.add(recipeMap);
                            }

                            Listener.onRecipeListReceived(recipeList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public ArrayList<Map<SpannableString, String>> getSorted () {
        return sortRecipeInstance.sortRecipes(this);
    }

    public sortRecipe getSortRecipeInstance() {
        return sortRecipeInstance;
    }

    public void setSortRecipeInstance(sortRecipe sortRecipeInstance) {
        this.sortRecipeInstance = sortRecipeInstance;
    }

    private ArrayList<Map<SpannableString, Recipe>> convertToExpectedFormat(List<Recipe> sortedRecipes) {
        ArrayList<Map<SpannableString, Recipe>> formattedList = new ArrayList<>();
        for (Recipe recipe : sortedRecipes) {
            SpannableString recipeNameSpannable = new SpannableString(recipe.getRecipeName());
            // Optionally, you could apply formatting to recipeNameSpannable here
            Map<SpannableString, Recipe> recipeMap = new HashMap<>();
            recipeMap.put(recipeNameSpannable, recipe);
            formattedList.add(recipeMap);
        }
        return formattedList;
    }

    public void fetchAndSortRecipesByIngredientsAvailability(RecipeListListener listener) {
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
                        Log.d("FetchData", "Failed to parse a Recipe from snapshot: " + recipeSnapshot.getValue());
                    }
                    if (recipe != null) {
                        doesUserHaveIngredients(recipe, hasIngredients -> {
                            if (hasIngredients) {
                                recipesWithIngredients.add(recipe);
                            } else {
                                recipesWithoutIngredients.add(recipe);
                            }

                            if (recipesWithIngredients.size() + recipesWithoutIngredients.size() == dataSnapshot.getChildrenCount()) {
                                Log.d("FetchData", "All ingredient checks completed. With ingredients: " + recipesWithIngredients.size() + ", Without ingredients: " + recipesWithoutIngredients.size());
                                sortedRecipes.addAll(recipesWithIngredients);
                                ArrayList<Map<SpannableString, Recipe>> finalList = convertToExpectedFormat(sortedRecipes);
                                Log.d("FetchData", "Invoking listener with sorted recipes.");
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
}
