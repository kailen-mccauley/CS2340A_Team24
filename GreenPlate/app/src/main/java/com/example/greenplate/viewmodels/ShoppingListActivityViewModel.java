package com.example.greenplate.viewmodels;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.greenplate.models.Ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

     /* TODO1: Add to shopping list
     * - Take in ingredient name, quantity
     * - Query database to see if there is another shopping list item w/ same name
     * - If there is, then simply update the quantity
     * - Otherwise, add a new shopping list item to database
     */


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
}