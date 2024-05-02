package com.example.greenplate.viewmodels;

import com.example.greenplate.models.User;


import androidx.annotation.NonNull;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


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

    private int calculateRecommendedWorkoutTime(double weightCalc, double heightCalc) {
        double bmi = weightCalc / Math.pow((heightCalc / 100), 2);
        if (bmi < 18.5) {
            return 30;
        } else if (bmi < 25) {
            return 60;
        } else if (bmi < 30) {
            return 75;
        } else {
            return 90;
        }
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
            int stepGoal;
            int weightCalc = Integer.parseInt(weight);
            int heightCalc = Integer.parseInt(height);
            int workoutGoal = calculateRecommendedWorkoutTime(weightCalc, heightCalc);
            if (gender.equals("M")) {
                calGoal = 10 * weightCalc + 6 * heightCalc + 5;
                stepGoal = 10000;
            } else {
                calGoal = 10 * weightCalc + 6 * heightCalc - 141;
                stepGoal = 7500;
            }
            updateUser(height, weight, gender, calGoal, stepGoal, workoutGoal);
            Map<String, Object> userData = new HashMap<>();
            userData.put("userHeight", height);
            userData.put("userWeight", weight);
            userData.put("userGender", gender);
            userData.put("calorieGoal", calGoal);
            userData.put("stepGoal", stepGoal);
            userData.put("workoutGoal", workoutGoal);
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mDatabase.child("users").child(uid).updateChildren(userData);
                    } else {
                        mDatabase.child("users").child(uid).setValue(getUserData());                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    public void getUserInfo(UserInfoCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("users").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                try {
                                    String height = dataSnapshot.child("userHeight")
                                            .getValue(String.class);
                                    String weight = dataSnapshot.child("userWeight")
                                            .getValue(String.class);
                                    String gender = dataSnapshot.child("userGender")
                                            .getValue(String.class);
                                    int calorieGoal = dataSnapshot.child("calorieGoal")
                                            .getValue(Integer.class);
                                    int stepGoal = dataSnapshot.child("stepGoal")
                                            .getValue(Integer.class);
                                    int workoutGoal = dataSnapshot.child("workoutGoal")
                                            .getValue(Integer.class);
                                    updateUser(height, weight, gender,
                                            calorieGoal, stepGoal, workoutGoal);
                                    callback.onUserInfoReceived(getUserData());
                                } catch (Exception e) {
                                    callback.onUserInfoReceived(null);
                                }
                            } else {
                                callback.onUserInfoReceived(null);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                            callback.onUserInfoReceived(null);
                        }
                    });
        } else {

            callback.onUserInfoReceived(null);
        }
    }
    public User getUserData() {
        return userData;
    }
    public void updateUser(String height, String weight, String gender,
                           int calorieGoal, int stepGoal, int workoutGoal) {
        getUserData().setUserHeight(height);
        getUserData().setUserWeight(weight);
        getUserData().setUserGender(gender);
        getUserData().setCalorieGoal(calorieGoal);
        getUserData().setStepGoal(stepGoal);
        getUserData().setWorkoutGoalInMinutes(workoutGoal);
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
