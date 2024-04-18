package com.example.greenplate.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.FitnessActivityObserver;
import com.example.greenplate.utilities.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.utilities.ValueExtractor;

import com.example.greenplate.viewmodels.FitnessActivityViewModel;

import java.util.Date;
import java.util.Locale;


public class FitnessActivity extends AppCompatActivity  implements FitnessActivityObserver {
    private TextView activityGoalTextView;
    private TextView timerTextView;
    private Button startStopButton;
    private EditText stepsEditText;

    private Handler timerHandler = new Handler();
    private boolean stopwatchRunning = false;
    private int seconds = 0;

    private FitnessActivityViewModel fitnessVM;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);

        Intent intentHome = new Intent(FitnessActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentMeal = new Intent(FitnessActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentRecipe = new Intent(FitnessActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentShopping = new Intent(FitnessActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentPersonal = new Intent(FitnessActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentIngredient = new Intent(FitnessActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);

        fitnessVM = FitnessActivityViewModel.getInstance();
        fitnessVM.addObserver(this);

        activityGoalTextView = findViewById(R.id.activityGoalTextView);
        timerTextView = findViewById(R.id.timerTextView);

        startStopButton = findViewById(R.id.startStopButton);
        Button saveActivityButton = findViewById(R.id.saveActivityButton);

        stepsEditText = findViewById(R.id.stepsEditText);
        Button saveStepsButton = findViewById(R.id.saveStepsButton);

        Intent wellnessIntent = getIntent();
        Bundle extras = wellnessIntent.getExtras();
        // We need this for if a user wants to set an activity goal that can be seen here.
        // Don't necessarily need this if we do the activity rings on the home page.
        if (extras != null) {
        }

        startStopButton.setOnClickListener(v -> {
            runTimer();
            if (stopwatchRunning) {
                startStopButton.setText("Start");
                stopwatchRunning = false;
            } else {
                stopwatchRunning = true;
                startStopButton.setText("Stop");
            }

        });

        saveActivityButton.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("fitness");
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String time = ValueExtractor.extract(timerTextView);
                if (!InputValidator.isValidTime(time) || stopwatchRunning) {
                    Toast.makeText(FitnessActivity.this, "Please enter a valid time or stop the stopwatch.", Toast.LENGTH_SHORT).show();
                    return;
                }

                fitnessVM.storeActivity(time);
                Toast.makeText(FitnessActivity.this,
                        "Activity added!",
                        Toast.LENGTH_SHORT).show();

                firebaseRef.child(today).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild("streakUpdated")) {
                            String yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
                            firebaseRef.child(yesterday).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    SharedPreferences prefs = getSharedPreferences("FitnessPrefs_" + userId, Context.MODE_PRIVATE);
                                    int currentStreak = prefs.getInt("streak", 0);

                                    if (dataSnapshot.exists()) {
                                        currentStreak++;
                                    } else {
                                        currentStreak = 1;
                                    }

                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt("streak", currentStreak);
                                    editor.apply();

                                    firebaseRef.child(today).child("streakUpdated").setValue(true);
                                    firebaseRef.child("streak").setValue(currentStreak);

                                    Toast.makeText(FitnessActivity.this, "Streak updated to: " + currentStreak, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else {
                Toast.makeText(FitnessActivity.this, "No authenticated user found.", Toast.LENGTH_SHORT).show();
            }
        });

        saveStepsButton.setOnClickListener(v -> {
            String steps = ValueExtractor.extract(stepsEditText);
            if (!InputValidator.isValidInputWithInteger(steps)) {
                Toast.makeText(FitnessActivity.this,
                        "Please enter a valid step count!",
                        Toast.LENGTH_SHORT).show();
            } else {
                int stepNum = Integer.parseInt(steps);
                fitnessVM.storeSteps(stepNum);

                Toast.makeText(FitnessActivity.this,
                        "Steps tracked!",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void runTimer() {
        new Thread(() -> {
            while (stopwatchRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds++;
                timerHandler.post(() -> timerTextView.setText(formatTime(seconds)));
            }
        }).start();
    }

    private String formatTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    @Override
    public void updateTimerUI() {
        seconds = 0;
        timerTextView.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
    }

    @Override
    public void updateStepsUI() {
        stepsEditText.setText("");
    }
}

