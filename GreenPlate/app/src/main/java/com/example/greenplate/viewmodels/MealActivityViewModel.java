package com.example.greenplate.viewmodels;

import com.example.greenplate.models.Meal;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;

//ADDED
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;


public class MealActivityViewModel {

    private static volatile MealActivityViewModel instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private int calculatedCalorieGoal;

    private MealActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    public static MealActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (MealActivityViewModel.class) {
                if (instance == null) {
                    instance = new MealActivityViewModel();
                }
            }
        }
        return instance;
    }

    // Method to store meal information in Firebase Database
    public void storeMeal(String mealName, String calories) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String mealId = mDatabase.child("meals").push().getKey();

            // Get the current date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());

            // Create a Meal object with mealName, calories, and userId
            Meal meal = new Meal(mealName, calories, uid, currentDate);

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

    // Method to calculate and retrieve daily calorie intake
    public void getDailyCalorieIntake(DailyCalorieIntakeCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());

            DatabaseReference mealsRef = mDatabase.child("meals");
            mealsRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int totalCalories = 0;
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.exists()) {
                                String date = dataSnapshot.child("date").getValue(String.class);
                                if (date.equals(currentDate)) {
                                    totalCalories += Integer.parseInt(dataSnapshot.child("calories")
                                            .getValue(String.class));
                                }
                            }
                        }
                    }
                    callback.onDailyCalorieIntakeReceived(totalCalories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    callback.onDailyCalorieIntakeReceived(0); // or -1 to indicate error
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getEveryCalorieIntake(EveryCalorieIntakeCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            String currentMonth = dateFormat.format(calendar.getTime());

            DatabaseReference mealsRef = mDatabase.child("meals");
            List<Integer> calorieIntakeList = new ArrayList<>();

            YearMonth currentYearMonth = YearMonth.now();

            int daysInMonth = currentYearMonth.lengthOfMonth();

            for (int i = 1; i <= daysInMonth; i++) {
                String currentDate = currentMonth + "-" + String.format(Locale.getDefault(), "%02d", i);
                final int[] totalCalories = {0};
                mealsRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String date = dataSnapshot.child("date").getValue(String.class);
                                if (date != null && date.startsWith(currentDate)) {
                                    totalCalories[0] += Integer.parseInt(dataSnapshot.child("calories").getValue(String.class));
                                }
                            }
                        }
                        calorieIntakeList.add(totalCalories[0]);

                        if (calorieIntakeList.size() == daysInMonth) {
                            callback.onEveryCalorieIntakeReceived(calorieIntakeList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onEveryCalorieIntakeReceived(new ArrayList<>()); // or -1 to indicate error
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAverageCalorieIntake(AverageCalorieIntakeCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            String currentMonth = dateFormat.format(calendar.getTime());

            DatabaseReference mealsRef = mDatabase.child("meals");
            mealsRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int allCalories = 0;
                    String currentDate = null;
                    int daysData = 0;
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String date = dataSnapshot.child("date").getValue(String.class);
                            if (date != null && date.startsWith(currentMonth)) {
                                allCalories += Integer.parseInt(dataSnapshot.child("calories").getValue(String.class));
                                if (!date.equals(currentDate)) {
                                    daysData++;
                                    currentDate = date;
                                }
                            }
                        }
                    }
                    Log.d("DEBUG", "Total Calories: " + allCalories);
                    Log.d("DEBUG", "Days with Data: " + daysData);
                    if (daysData > 0) {
                        int averageCalories = allCalories / daysData;
                        callback.onAverageCalorieIntakeReceived(averageCalories);
                    } else {
                        callback.onAverageCalorieIntakeReceived(-1); // Error: No data available
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onAverageCalorieIntakeReceived(-1); // Error occurred
                }
            });
        }
    }

    // Define a callback interface for returning daily calorie intake
    public interface DailyCalorieIntakeCallback {
        void onDailyCalorieIntakeReceived(int totalCalories);
    }

    public interface EveryCalorieIntakeCallback {
        void onEveryCalorieIntakeReceived(List<Integer> totalCalories);
    }

    public interface AverageCalorieIntakeCallback {
        void onAverageCalorieIntakeReceived(int averageCalories);
    }
}
