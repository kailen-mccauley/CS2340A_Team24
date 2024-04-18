package com.example.greenplate.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FitnessActivityViewModel {

    private static volatile FitnessActivityViewModel instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<FitnessActivityObserver> observers = new ArrayList<>();

    private FitnessActivityViewModel() {
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static FitnessActivityViewModel getInstance() {
        if (instance == null) {
            synchronized (FitnessActivityViewModel.class) {
                if (instance == null) {
                    instance = new FitnessActivityViewModel();
                }
            }
        }
        return instance;
    }


    public void storeActivity(String activityTime) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentTime = timeFormat.format(Calendar.getInstance().getTime());
            mDatabase.child("users").child(uid).child("fitness").child(currentDate)
                    .child("Activity").child(currentTime).setValue(activityTime)
                    .addOnSuccessListener(aVoid -> Log.d("storeActivity",
                    "Activity successfully stored."))
                    .addOnFailureListener(e -> Log.d("storeActivity",
                            "Failed to store Activity.", e));
            notifyTimerObservers();
        } else {
            Log.d("storeRecipe", "No authenticated user found.");
        }
    }

    public void storeSteps(int steps) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());

            getStepsForDate(uid, currentDate, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int stepsCount = snapshot.getValue(Integer.class);
                        int updatedsteps = stepsCount + steps;
                        mDatabase.child("users").child(uid).child("fitness").child(currentDate)
                                .child("Steps").setValue(updatedsteps)
                                .addOnSuccessListener(aVoid -> Log.d("storeSteps",
                                        "Steps successfully stored."))
                                .addOnFailureListener(e -> Log.d("storeSteps",
                                        "Failed to store Steps.", e));
                        notifyStepsObservers();
                    } else {
                        mDatabase.child("users").child(uid).child("fitness")
                                .child(currentDate)
                                .child("Steps").setValue(steps)
                                .addOnSuccessListener(aVoid -> Log.d("storeSteps",
                                        "Steps successfully stored."))
                                .addOnFailureListener(e -> Log.d("storeSteps",
                                        "Failed to store Steps.", e));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Log.d("storeSteps", "No authenticated user found.");
        }
    }

    public void getStepsForDate(String userId, String date, ValueEventListener listener) {
        mDatabase.child("users").child(userId)
                .child("fitness").child(date).child("Steps")
                .addListenerForSingleValueEvent(listener);
    }

    private long parseDurationStringToMinutes(String durationString) {
        String[] parts = durationString.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return hours * 60 + minutes + (seconds > 0 ? 1 : 0);
        // rounds up if there are remaining seconds
    }

    public void getActivityforDate(DailyActivityCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(Calendar.getInstance().getTime());


            mDatabase.child("users").child(uid).child("fitness")
                    .child(currentDate).child("Activity")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long totalActivityTimeMinutes = 0;
                            for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                                String durationString = activitySnapshot.getValue(String.class);
                                long durationMinutes = parseDurationStringToMinutes(durationString);
                                totalActivityTimeMinutes += durationMinutes;
                            }
                            callback.onDailyActivityRecieved(totalActivityTimeMinutes);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //if yk yk
                        }
                    });
        }
    }
    public void addObserver(FitnessActivityObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(FitnessActivityObserver observer) {
        observers.remove(observer);
    }

    public void notifyTimerObservers() {
        for (FitnessActivityObserver observer : observers) {
            observer.updateTimerUI();
        }
    }
    public void notifyStepsObservers() {
        for (FitnessActivityObserver observer : observers) {
            observer.updateStepsUI();
        }
    }

    public interface DailyActivityCallback {
        void onDailyActivityRecieved(long dailyActivity);
    }

    //to use:
    //    viewModel.getActivityforDate(new FitnessActivityViewModel.DailyActivityCallback() {
    //        @Override
    //        public void onDailyActivityRecieved(long DailyActivity) {
    //            //do something
    //        }
    //    });

}
