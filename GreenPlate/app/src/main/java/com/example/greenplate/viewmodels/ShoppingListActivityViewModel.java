package com.example.greenplate.viewmodels;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.ShoppingItem;
import com.example.greenplate.views.ShoppingActivity;
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
        if (currentUser != null) {
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

    public void storeShoppingListItem(String ingredientName, int quantity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isDuplicate = false;
                        String id = null;
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String name = snapshot.getValue(ShoppingItem.class).getIngredientName();
                            if (name != null && name.equals(ingredientName.toLowerCase())) {
                                isDuplicate = true;
                                id = snapshot.getKey();
                                break;
                            }
                        }

                        if (isDuplicate) {
                            mDatabase.child("shoppinglist").child(id).child("quantity").setValue(quantity);
                            //Toast.makeText(shoppingActivity, "Item already in shopping cart; quantity updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            id = mDatabase.child("shoppinglist").push().getKey();
                            ShoppingItem item = new ShoppingItem(ingredientName, quantity, uid);
                            mDatabase.child("shoppinglist").child(id).setValue(item);
                            //Toast.makeText(shoppingActivity, "Item added to shopping cart!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    }

    public void removeShoppingListItem(String ingredientName) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            String name = snapshot.getValue(ShoppingItem.class).getIngredientName();
                            if (name != null && name.equals(ingredientName.toLowerCase())) {
                                String id = snapshot.getKey();
                                mDatabase.child("shoppinglist").child(id).removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }
    }
    
    public interface ShoppingItemsListener {
        void onShoppingItemsReceived(Map<String, Integer> shoppingListItems);
    }

     /* TODO4: Buy items
     * - Take in a list of items by name
     * - Remove each item from shopping list and add to pantry
     */
     public void buyItems(List<String> itemNames) {
         FirebaseUser currentUser = mAuth.getCurrentUser();
         if (currentUser != null) {
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
                                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                     boolean ingredientExists = false;
                                                     String pantryItemName = null;
                                                     for (DataSnapshot pantryItemSnapshot : snapshot.getChildren()) {
                                                         Ingredient pantryItem = pantryItemSnapshot.getValue(Ingredient.class);
                                                         if (pantryItem != null && pantryItem.getUserId().equals(uid)) {
                                                             ingredientExists = true;
                                                             pantryItemName = pantryItemSnapshot.getKey();
                                                             int newQuantity = pantryItem.getQuantity() + item.getQuantity();
                                                             mDatabase.child("pantry").child(pantryItemName).child("quantity").setValue(newQuantity);
                                                             break;
                                                         }
                                                     }

                                                     if (!ingredientExists) {
                                                         String newPantryItemId = mDatabase.child("pantry").push().getKey();
                                                         if (newPantryItemId != null) {
                                                             Ingredient pantryItem = new Ingredient(
                                                                     item.getIngredientName(),
                                                                     0,
                                                                     item.getQuantity(),
                                                                     uid
                                                             );
                                                             mDatabase.child("pantry").child(newPantryItemId).setValue(pantryItem);
                                                         }
                                                     }
                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                 }
                                             });
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
         }
     }

}
