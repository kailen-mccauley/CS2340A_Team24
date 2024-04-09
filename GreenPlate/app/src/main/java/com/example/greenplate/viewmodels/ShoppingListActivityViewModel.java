package com.example.greenplate.viewmodels;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
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
                            String ingredientName = snapshot.child("ingredientName").getValue(String.class);
                            int quantity = snapshot.child("quantity").getValue(Integer.class);
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

    public void storeShoppingListItem(String ingredientName, int quantity, ShoppingActivity shoppingActivity) {
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
                            String name = snapshot.child("ingredientName").getValue(String.class);
                            if (name != null && name.equals(ingredientName.toLowerCase())) {
                                isDuplicate = true;
                                id = snapshot.getKey();
                                break;
                            }
                        }

                        if (isDuplicate) {
                            mDatabase.child("shoppinglist").child(id).child("quantity").setValue(quantity);
                            Toast.makeText(shoppingActivity, "Item already in shopping cart; quantity updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            id = mDatabase.child("shoppinglist").push().getKey();
                            mDatabase.child("shoppinglist").child(id).child("quantity").setValue(quantity);
                            mDatabase.child("shoppinglist").child(id).child("ingredientName").setValue(ingredientName.toLowerCase());
                            mDatabase.child("shoppinglist").child(id).child("userId").setValue(uid);
                            Toast.makeText(shoppingActivity, "Item added to shopping cart!", Toast.LENGTH_SHORT).show();
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
                            String name = snapshot.child("ingredientName").getValue(String.class);
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


     /* TODO2: Increase quantity
     * - Take in ingredient name, quantity
     * - Increment quantity
     */
     public void increaseShoppingListQuantity(String ingredientName, int quantity) {
         FirebaseUser currentUser = mAuth.getCurrentUser();
         if (currentUser != null) {
             String uid = currentUser.getUid();
             mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                     .addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                 Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                 if (ingredient != null && ingredient.getIngredientName()
                                         .equals(ingredientName)) {
                                     String itemID = snapshot.getKey();
                                     int newQuantity = quantity + ingredient.getQuantity();
                                     //ADD FUNCTIONALITY IN VIEW SO THAT IT TESTS USING INPUT VALIDATOR THAT THE QUANTITY CANNOT BE NEGATIVE
                                     mDatabase.child("shoppinglist").child(itemID)
                                             .child("quantity").setValue(newQuantity);
                                     return; // Exit loop once ingredient is found and updated
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
         }
     }

     /* TODO3: Decrease quantity
     * - Same thing, but decrease
     * - Edge case: if quantity drops to 0 or below, remove it from database
     */
     public void decreaseShoppingListQuantity(String ingredientName, int quantity) {
         FirebaseUser currentUser = mAuth.getCurrentUser();
         if (currentUser != null) {
             String uid = currentUser.getUid();
             mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                     .addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                 Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                 if (ingredient != null && ingredient.getIngredientName()
                                         .equals(ingredientName)) {
                                     String itemID = snapshot.getKey();
                                     int newQuantity = ingredient.getQuantity() - quantity;
                                     if (newQuantity <= 0) {
                                         mDatabase.child("shoppinglist").child(itemID).removeValue();
                                     } else {
                                         mDatabase.child("shoppinglist").child(itemID)
                                                 .child("quantity").setValue(newQuantity);
                                     }
                                     return; // Exit loop once ingredient is found and updated
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
         }
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
                                 Ingredient ingredient = snapshot.getValue(Ingredient.class);
                                 if (ingredient != null && itemNames.contains(ingredient.getIngredientName())) {
                                     snapshot.getRef().removeValue();

                                     mDatabase.child("pantry").orderByChild("ingredientName")
                                             .equalTo(ingredient.getIngredientName())
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
                                                             int newQuantity = pantryItem.getQuantity() + ingredient.getQuantity();
                                                             mDatabase.child("pantry").child(pantryItemName).child("quantity").setValue(newQuantity);
                                                             break;
                                                         }
                                                     }

                                                     if (!ingredientExists) {
                                                         String newPantryItemId = mDatabase.child("pantry").push().getKey();
                                                         if (newPantryItemId != null) {
                                                             Ingredient pantryItem = new Ingredient(
                                                                     ingredient.getIngredientName(),
                                                                     ingredient.getCalories(),
                                                                     ingredient.getQuantity(),
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
