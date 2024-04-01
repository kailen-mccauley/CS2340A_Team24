package com.example.greenplate;

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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeAlphabeticalSorter implements SortRecipe {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public RecipeAlphabeticalSorter() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }
    public void sortRecipes(RecipeActivityViewModel.RecipeListListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ArrayList<Map<SpannableString, Recipe>> recipeList = new ArrayList<>();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("cookbook").orderByChild("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                String recipeName = snapshot.child("name").getValue(String.class);
                                SpannableString name = new SpannableString(recipeName);
                                Map<String, Integer> ingredientsMap = (Map<String, Integer>)
                                        snapshot.child("ingredients").getValue();
                                String recipeID = snapshot.child("recipeID").getValue(String.class);
                                Recipe recipe = new Recipe(recipeName, ingredientsMap, recipeID);
                                Map<SpannableString, Recipe> recipeMap = new HashMap<>();
                                recipeMap.put(name, recipe);
                                recipeList.add(recipeMap);
                            }

                            listener.onRecipeListReceived(recipeList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}
