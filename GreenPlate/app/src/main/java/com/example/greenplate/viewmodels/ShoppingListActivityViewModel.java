package com.example.greenplate.viewmodels;


import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.ShoppingItem;
import com.example.greenplate.observers.ShoppingFormObserver;
import com.example.greenplate.utilites.ShoppingValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ShoppingListActivityViewModel {
    private static volatile ShoppingListActivityViewModel instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<ShoppingFormObserver> observers = new ArrayList<>();

    private ShoppingListActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static ShoppingListActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (ShoppingListActivityViewModel.class) {
                if (instance == null) {
                    instance = new ShoppingListActivityViewModel();
                }
            }
        }
        return instance;
    }

    public void fetchShoppingListItems(ShoppingItemsListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Map<String, Integer> items = new HashMap<>();
        if (ShoppingValidator.isValidUser(currentUser)) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                            String ingredientName = item.getIngredientName();
                            int quantity = item.getQuantity();
                            items.put(ingredientName, quantity);
                        }
                        listener.onShoppingItemsReceived(items);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    }

    public void storeShoppingListItem(String ingredientName, int quantity, int calories,
                                      StoreItemListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isDuplicate = false;
                            String id;
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                String name =
                                        snapshot.getValue(ShoppingItem.class).getIngredientName();
                                if (name != null && name.equals(ingredientName.toLowerCase())) {
                                    isDuplicate = true;
                                    id = snapshot.getKey();
                                    int currQuantity =
                                            snapshot.getValue(ShoppingItem.class).getQuantity();
                                    mDatabase.child("shoppinglist").child(id)
                                            .child("quantity")
                                            .setValue(quantity + currQuantity)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    listener.onShoppingItemStored();
                                                    notifyNewRecipeObservers();
                                                }
                                            });
                                    break;
                                }
                            }
                            if (!isDuplicate) {
                                id = mDatabase.child("shoppinglist").push().getKey();
                                ShoppingItem item = new ShoppingItem(ingredientName,
                                        quantity, uid, calories);
                                mDatabase.child("shoppinglist").child(id).setValue(item)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                listener.onShoppingItemStored();
                                                notifyNewRecipeObservers();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    public void removeShoppingListItem(String ingredientName, RemoveItemListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (ShoppingValidator.isValidUser(currentUser)
                && ShoppingValidator.isValidIngredient(ingredientName)) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                String name =
                                        snapshot.getValue(ShoppingItem.class).getIngredientName();
                                if (name != null && name.equals(ingredientName.toLowerCase())) {
                                    String id = snapshot.getKey();
                                    mDatabase.child("shoppinglist").child(id).removeValue()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    listener.onShoppingItemRemoved();
                                                }
                                            });
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    public void buyItems(List<String> itemNames, BuyItemsListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (ShoppingValidator.isValidUser(currentUser)
                && ShoppingValidator.isValidBuyItemsList(itemNames)) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                                if (item != null && itemNames.contains(item.getIngredientName())) {
                                    snapshot.getRef().removeValue();

                                    mDatabase.child("pantry").orderByChild("ingredientName")
                                            .equalTo(item.getIngredientName())
                                            .addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(
                                                            @NonNull DataSnapshot snapshot) {
                                                        boolean ingredientExists = false;
                                                        String pantryItemName = null;
                                                        for (DataSnapshot pantryItemSnapshot
                                                                : snapshot.getChildren()) {
                                                            Ingredient pantryItem
                                                                    = pantryItemSnapshot
                                                                    .getValue(Ingredient.class);
                                                            if (pantryItem != null && pantryItem.
                                                                    getUserId().equals(uid)) {
                                                                ingredientExists = true;
                                                                pantryItemName = pantryItemSnapshot
                                                                        .getKey();
                                                                int newQuantity = pantryItem
                                                                        .getQuantity()
                                                                        + item.getQuantity();
                                                                mDatabase.child("pantry")
                                                                        .child(pantryItemName)
                                                                        .child("quantity")
                                                                        .setValue(newQuantity);
                                                                break;
                                                            }
                                                        }

                                                        if (!ingredientExists) {
                                                            String newPantryItemId
                                                                    = mDatabase.child("pantry")
                                                                    .push().getKey();
                                                            if (newPantryItemId != null) {
                                                                Ingredient pantryItem = new
                                                                        Ingredient(item
                                                                        .getIngredientName(),
                                                                        item.getCalories(),
                                                                        item.getQuantity(), uid);
                                                                mDatabase.child("pantry")
                                                                        .child(newPantryItemId)
                                                                        .setValue(pantryItem);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(
                                                            @NonNull DatabaseError error) {
                                                    }
                                                });
                                }
                            }
                            listener.onShoppingItemsBought();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }
    public void getCalories(String ingredientName, CalorieListener listener) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("pantry").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot pantrySnapshot) {
                            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(
                                                @NonNull DataSnapshot shoppingSnapshot) {
                                            boolean found = false;
                                            int calories = 0;

                                            for (DataSnapshot snapshot: pantrySnapshot
                                                    .getChildren()) {
                                                String name = snapshot.getValue(Ingredient.class)
                                                        .getIngredientName();
                                                if (name != null && name.equals(ingredientName
                                                        .toLowerCase())) {
                                                    calories = snapshot.getValue(Ingredient.class)
                                                            .getCalories();
                                                    listener.onCalorieResult(calories);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                for (DataSnapshot snapshot
                                                        : shoppingSnapshot.getChildren()) {
                                                    String name = snapshot.getValue(ShoppingItem
                                                                    .class).getIngredientName();
                                                    if (name != null && name.equals(ingredientName
                                                            .toLowerCase())) {
                                                        calories = snapshot.getValue(ShoppingItem
                                                                        .class).getCalories();
                                                        listener.onCalorieResult(calories);
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (!found) {
                                                listener.onCalorieResult((int)
                                                        (Math.random() * 46) + 5);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull
                                                                DatabaseError shoppingError) {
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError pantryError) {
                        }
                    });
        }
    }

    public interface CalorieListener {
        void onCalorieResult(Integer calories);
    }
    public interface ShoppingItemsListener {
        void onShoppingItemsReceived(Map<String, Integer> shoppingListItems);
    }
    public interface StoreItemListener {
        void onShoppingItemStored();
    }
    public interface RemoveItemListener {
        void onShoppingItemRemoved();
    }
    public interface BuyItemsListener {
        void onShoppingItemsBought();
    }
    public void addObserver(ShoppingFormObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ShoppingFormObserver observer) {
        observers.remove(observer);
    }

    public void notifyNewRecipeObservers() {
        for (ShoppingFormObserver observer : observers) {
            observer.updateShoppingScrollable();
        }
    }

}
