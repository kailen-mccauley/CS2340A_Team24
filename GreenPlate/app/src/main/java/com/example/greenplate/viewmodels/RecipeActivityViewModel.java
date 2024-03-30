package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Meal;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.sortRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.SpannableString;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private sortRecipe sortRecipeInstance;
    private RecipeActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recipeData = new Recipe();
        //sortRecipeInstance = sortRecipeAlphabetical;
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
            DatabaseReference ingredientsRef = mDatabase.child("pantry").child(uid);
            Map<String, Integer> recipeIngredients = recipe.getIngredients();

            ingredientsRef.equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Check if the user has all ingredients required for the recipe
                    boolean hasAllIngredients = true;
                    for (Map.Entry<String, Integer> entry : recipeIngredients.entrySet()) {
                        String ingredientName = entry.getKey();
                        int requiredQuantity = entry.getValue();
                        // Check if the user has this ingredient and has enough quantity
                        if (!snapshot.hasChild(ingredientName) || snapshot.child(ingredientName).getValue(Integer.class) < requiredQuantity) {
                            hasAllIngredients = false;
                            break; // No need to continue checking if any ingredient is missing
                        }
                    }
                    // Call the listener with the result
                    listener.onIngredientCheckResult(hasAllIngredients);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Log.d("doesUserHaveIngredients", "Failed to read user ingredients from database.");
                    // Call the listener with a default result (false)
                    listener.onIngredientCheckResult(false);
                }
            });
        } else {
            Log.d("doesUserHaveIngredients", "No authenticated user found.");
            // Call the listener with a default result (false)
            listener.onIngredientCheckResult(false);
        }
    }



    //TODO: withUserIngredients
    // creates a list of recipe objects that a user can make.
//    public List<Recipe> withUserIngredients() {
//        List<Recipe> userCanMakeRecipes = new ArrayList<>();
//        DatabaseReference recipeRef = mDatabase.child("cookbook");
//        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
//                    String recipeName = recipeSnapshot.child("name").getValue(String.class);
//                    Map<String, Integer> ingredientsMap = (Map<String, Integer>) recipeSnapshot.child("ingredients").getValue();
//                    String recipeID = recipeSnapshot.child("recipeID").getValue(String.class);
//                    Recipe recipe = new Recipe(recipeName, ingredientsMap, recipeID);
//                    if (doesUserHaveIngredients(recipe)) {
//                        userCanMakeRecipes.add(recipe);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("withUserIngredients", "Failed to read recipes from database.");
//            }
//        });
//        return userCanMakeRecipes;
//    }

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
}
