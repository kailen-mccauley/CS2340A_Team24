package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.ValueExtractor;

import com.example.greenplate.viewmodels.FitnessActivityViewModel;

import java.util.ArrayList;


public class FitnessActivity extends AppCompatActivity {
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
            String time = ValueExtractor.extract(timerTextView);
            System.out.println(time);
            if (!InputValidator.isValidTime(time)) {
                return;
            }
            String[] brokenDownTime = time.split(":");
            int[] times = new int[3];
            for (int i = 0; i < brokenDownTime.length; i++) {
                times[i] = Integer.parseInt(brokenDownTime[i]);
                System.out.println(times[i]);
            }
            ArrayList<Integer> timesList = new ArrayList<>();
            for (int i = 0; i < brokenDownTime.length; i++) {
                timesList.add(Integer.parseInt(brokenDownTime[i]));
                System.out.println(timesList.get(i));
            }
            System.out.println(timesList);
            fitnessVM.storeActivity(time);
            Toast.makeText(FitnessActivity.this,
                    "Activity logged!",
                    Toast.LENGTH_SHORT).show();
            stepsEditText.setText("");
            timerTextView.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
            seconds = 0;
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
                stepsEditText.setText("");
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
}

