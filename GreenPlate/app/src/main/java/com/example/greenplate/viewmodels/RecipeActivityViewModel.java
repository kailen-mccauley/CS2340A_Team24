package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}