package com.example.greenplate.viewmodels;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MealActivityViewModel {

    private static MealActivityViewModel instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private int calculatedCalorieGoal;

//    public interface MealDataCallback {
//        void onMealDataReceived(int day, int calories);
//    }

    private MealActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public static synchronized MealActivityViewModel getInstance() {
        if (instance == null) {
            instance = new MealActivityViewModel();
        }
        return instance;
    }
    // Method to store meal information in Firebase Database
    public void storeMeal(String mealName, String calories) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String mealId = mDatabase.child("meals").push().getKey();

            // Create a Meal object with mealName, calories, and userId
            Meal meal = new Meal(mealName, calories, uid);

            // Store the meal in the database under "meals" node
            mDatabase.child("meals").child(mealId).setValue(meal);
        }
    }

    // Method to query meals associated with the current user
    public void queryUserMeals() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            mDatabase.child("meals").orderByChild("userId").equalTo(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }


    public class Meal {
        private String userId; // To link meal to user
        private String mealName;
        private String calories;

        public Meal() {
            // Default constructor required for Firebase
        }

        public Meal(String mealName, String calories, String userId) {
            this.mealName = mealName;
            this.calories = calories;
            this.userId = userId;
        }


        public String getMealName() {
            return mealName;
        }

        public void setMealName(String mealName) {
            this.mealName = mealName;
        }

        public String getCalories() {
            return calories;
        }

        public void setCalories(String calories) {
            this.calories = calories;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }
}
