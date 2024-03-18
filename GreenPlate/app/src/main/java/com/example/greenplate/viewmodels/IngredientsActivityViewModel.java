package com.example.greenplate.viewmodels;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Meal;
import com.example.greenplate.models.User;
import com.example.greenplate.views.IngredientsActivity;
import com.example.greenplate.views.IngredientsFormActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;


public class IngredientsActivityViewModel {
    private static volatile IngredientsActivityViewModel instance;
    private final Ingredient ingredientData;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private IngredientsActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ingredientData = new Ingredient();
    }

    public static IngredientsActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (IngredientsActivityViewModel.class) {
                if (instance == null) {
                    instance = new IngredientsActivityViewModel();
                }
            }
        }
        return instance;
    }

    public void storeIngredient(String ingredientName, int calories, int quantity, IngredientsFormActivity IngredientsFormActivity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isDuplicate = false;
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                if (ingredient != null && ingredient.getIngredientName().equals(ingredientName)) {
                                    isDuplicate = true;
                                    break;
                                }
                            }

                            if (!isDuplicate) {
                                String ingredientId = mDatabase.child("pantry").push().getKey();
                                Ingredient newIngredient = new Ingredient(ingredientName, calories, quantity, uid);
                                mDatabase.child("pantry").child(ingredientId).setValue(newIngredient);
                                Toast.makeText(IngredientsFormActivity,
                                        "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(IngredientsFormActivity,
                                        "Duplicate Ingredient!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    public interface IngredientListListener {
        void onIngredientsReceived(ArrayList<Ingredient> sortedIngredients);

    }

    public void getSortedIngredients(IngredientListListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ArrayList<Ingredient> sortedIngredient = new ArrayList<>();
        if (currentUser!= null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class); // Assuming Ingredient class exists
                                sortedIngredient.add(ingredient);
                            }

                            Collections.sort(sortedIngredient, new Comparator<Ingredient>() {
                                @Override
                                public int compare(Ingredient ingredient1, Ingredient ingredient2) {
                                    return ingredient1.getIngredientName().compareToIgnoreCase(ingredient2.getIngredientName());
                                }
                            });

                            listener.onIngredientsReceived(sortedIngredient);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public interface IngredientMapListener {
        void onIngredientMapReceived(Map<String, Ingredient> ingredientMap);
    }
    public void getIngredientsMap(IngredientMapListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String,Ingredient> ingredientsMap = new HashMap();
        if (currentUser!= null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class); // Assuming Ingredient class exists
                                ingredientsMap.put(ingredient.getIngredientName(), ingredient);
                            }

                            listener.onIngredientMapReceived(ingredientsMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void updateIngredientQuantity(String ingredientId, int newQuantity) {
        mDatabase.child("pantry").child(ingredientId).child("quantity").setValue(newQuantity);
    }


}