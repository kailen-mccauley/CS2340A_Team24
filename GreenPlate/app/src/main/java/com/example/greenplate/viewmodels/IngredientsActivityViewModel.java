package com.example.greenplate.viewmodels;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
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
import java.util.Comparator;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


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

    public void storeIngredient(String ingredientName, int calories, int quantity,
                                IngredientsFormActivity ingredientsFormActivity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isDuplicate = false;
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                if (ingredient != null && ingredient.getIngredientName()
                                        .equals(ingredientName.toLowerCase())) {
                                    isDuplicate = true;
                                    break;
                                }
                            }

                            if (!isDuplicate) {
                                String ingredientId = mDatabase.child("pantry").push().getKey();
                                Ingredient newIngredient = new Ingredient(ingredientName
                                        .toLowerCase(), calories, quantity, uid);
                                mDatabase.child("pantry").child(ingredientId)
                                        .setValue(newIngredient);
                                Toast.makeText(ingredientsFormActivity,
                                        "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ingredientsFormActivity,
                                        "Duplicate Ingredient!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    public void getSortedIngredients(IngredientListListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ArrayList<Ingredient> sortedIngredient = new ArrayList<>();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                sortedIngredient.add(ingredient);
                            }

                            Collections.sort(sortedIngredient, new Comparator<Ingredient>() {
                                @Override
                                public int compare(Ingredient ingredient1, Ingredient ingredient2) {
                                    return ingredient1.getIngredientName()
                                            .compareToIgnoreCase(ingredient2.getIngredientName());
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

    //getIngredientsTreeMap
    public void getIngredTree(IngredientTreeMapListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Ingredient> ingredientsTreeMap = new TreeMap<>();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                ingredientsTreeMap.put(ingredient.getIngredientName(), ingredient);
                            }

                            listener.onIngredTreeReceive(ingredientsTreeMap);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    public void updateIngredientQuantity(String ingredientName, int newQuantity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ingredient ingredient = snapshot.getValue(Ingredient.class);
                            if (ingredient != null && ingredient.getIngredientName()
                                    .equals(ingredientName)) {
                                String ingredientId = snapshot.getKey();
                                int inputQuality = newQuantity + ingredient.getQuantity();
                                mDatabase.child("pantry").child(ingredientId)
                                        .child("quantity").setValue(inputQuality);
                                break; // Exit loop once ingredient is found and updated
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void updateIngredQuanTree(String ingredName, int newQuan, IngredientsActivityViewModel
            .IngredientTreeMapListener listener) {
        // Update the quantity in Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ingredient ingredient = snapshot.getValue(Ingredient.class);
                            if (ingredient != null && ingredient.getIngredientName()
                                    .equals(ingredName)) {
                                String ingredientId = snapshot.getKey();
                                int inputQuality = newQuan + ingredient.getQuantity();
                                mDatabase.child("pantry").child(ingredientId)
                                        .child("quantity").setValue(inputQuality);
                                break; // Exit loop once ingredient is found and updated
                            }
                        }
                        getIngredTree(new IngredientsActivityViewModel.IngredientTreeMapListener() {
                            @Override
                            public void onIngredTreeReceive(Map<String, Ingredient> ingredientMap) {
                                listener.onIngredTreeReceive(ingredientMap);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    public void decreaseIngrQuanTree(String ingredientName, int quan, IngredientsActivityViewModel
            .IngredientTreeMapListener listener, IngredientsActivity ingredientsActivity) {
        // Update the quantity in Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Ingredient ingredient = snapshot.getValue(Ingredient.class);
                            if (ingredient != null && ingredient.getIngredientName()
                                    .equals(ingredientName)) {
                                String ingredientId = snapshot.getKey();
                                int inputQuality =  ingredient.getQuantity() - quan;

                                if (inputQuality > 0) {
                                    mDatabase.child("pantry").child(ingredientId)
                                            .child("quantity").setValue(inputQuality);
                                    Toast.makeText(ingredientsActivity, "Quantity of "
                                            + ingredientName + " decreased by " + quan + "!",
                                            Toast.LENGTH_SHORT).show();
                                    break; // Exit loop once ingredient is found and updated
                                } else {
                                    mDatabase.child("pantry").child(ingredientId).removeValue();
                                    Toast.makeText(ingredientsActivity, "You're out of "
                                            + ingredient.getIngredientName(),
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        getIngredTree(new IngredientsActivityViewModel.IngredientTreeMapListener() {
                            @Override
                            public void onIngredTreeReceive(Map<String, Ingredient> ingredientMap) {
                                listener.onIngredTreeReceive(ingredientMap);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    public interface IngredientListListener {
        void onIngredientsReceived(ArrayList<Ingredient> sortedIngredients);

    }
    public interface IngredientTreeMapListener {
        void onIngredTreeReceive(Map<String, Ingredient> ingredientMap);
    }

}