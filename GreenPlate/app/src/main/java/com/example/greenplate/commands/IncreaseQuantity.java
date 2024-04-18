package com.example.greenplate.commands;

import androidx.annotation.NonNull;

import com.example.greenplate.models.ShoppingItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IncreaseQuantity implements ChangeQuantity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public IncreaseQuantity() {
        // Initialize Firebase componentss
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void changeQuantity(String ingredientName, int quantity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("shoppinglist").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                                if (item != null && item.getIngredientName()
                                        .equals(ingredientName)) {
                                    String itemID = snapshot.getKey();
                                    int newQuantity = quantity + item.getQuantity();
                                    // ADD FUNCTIONALITY IN VIEW SO THAT IT TESTS USING INPUT
                                    // VALIDATOR THAT THE QUANTITY CANNOT BE NEGATIVE
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
}
