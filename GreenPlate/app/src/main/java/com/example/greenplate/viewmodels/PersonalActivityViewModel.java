package com.example.greenplate.viewmodels;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PersonalActivityViewModel {
    private static PersonalActivityViewModel instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private PersonalActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public static synchronized PersonalActivityViewModel getInstance() {
        if (instance == null) {
            instance = new PersonalActivityViewModel();
        }
        return instance;
    }

    public void storePersonalInfo(String height, String weight, String gender) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            User user = new User(height, weight, gender);
            mDatabase.child("users").child(uid).setValue(user);
        }
    }
    public void getUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String height = user.getUserHeight();
                        String weight = user.getUserWeight();
                        String gender = user.getUserGender();
                        // Update UI with user information
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }


    public class User {

        private String userGender;
        private String userWeight;
        private String userHeight;

        public User() {}

        public User(String userHeight, String userWeight, String userGender) {
            this.setUserGender(userGender);
            this.setUserWeight(userWeight);
            this.setUserHeight(userHeight);

        }


        public String getUserGender() {
            return userGender;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public String getUserWeight() {
            return userWeight;
        }

        public void setUserWeight(String userWeight) {
            this.userWeight = userWeight;
        }

        public String getUserHeight() {
            return userHeight;
        }

        public void setUserHeight(String userHeight) {
            this.userHeight = userHeight;
        }
    }

}
