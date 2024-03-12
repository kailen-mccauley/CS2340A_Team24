package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Meal;
import com.example.greenplate.models.User;


import androidx.annotation.NonNull;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class PersonalActivityViewModel {
    private static volatile PersonalActivityViewModel instance;
    private final User userData;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private PersonalActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userData = new User();
    }

    public static PersonalActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (PersonalActivityViewModel.class) {
                if (instance == null) {
                    instance = new PersonalActivityViewModel();
                }
            }
        }
        return instance;
    }

    public void storePersonalInfo(String height, String weight, String gender) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            int calGoal;
            int weightCalc = Integer.parseInt(weight);
            int heightCalc = Integer.parseInt(height);
            if (gender.equals("M")) {
                calGoal = 10 * weightCalc + 6 * heightCalc + 5;
            } else {
                calGoal = 10 * weightCalc + 6 * heightCalc - 141;
            }
            updateUser(height, weight, gender, calGoal);
            mDatabase.child("users").child(uid).setValue(getUserData());
        }
    }
    public void getUserInfo(UserInfoCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String height = dataSnapshot.child("userHeight").getValue(String.class);
                        String weight = dataSnapshot.child("userWeight").getValue(String.class);
                        String gender = dataSnapshot.child("userGender").getValue(String.class);
                        int calorieGoal = dataSnapshot.child("calorieGoal").getValue(Integer.class);

                        updateUser(height, weight, gender, calorieGoal);

                        // Pass the user object to the callback
                        callback.onUserInfoReceived(getUserData());
                    } else {
                        // Handle case where user data doesn't exist
                        callback.onUserInfoReceived(null);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    // For example:
                    callback.onUserInfoReceived(null);
                }
            });
        } else {
            // Handle null current user
            // For example:
            callback.onUserInfoReceived(null);
        }
    }
    public User getUserData() {
        return userData;
    }
    public void updateUser(String height, String weight, String gender, int calorieGoal) {
        getUserData().setUserHeight(height);
        getUserData().setUserWeight(weight);
        getUserData().setUserGender(gender);
        getUserData().setCalorieGoal(calorieGoal);
    }

    public boolean isValidUser() {
        return getUserData().getUserHeight() != null
                && getUserData().getUserWeight() != null
                && getUserData().getUserGender() != null;
    }

    public interface UserInfoCallback {
        void onUserInfoReceived(User user);
    }



}
