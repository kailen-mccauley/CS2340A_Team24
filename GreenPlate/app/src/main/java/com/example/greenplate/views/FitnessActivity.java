package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.ValueExtractor;

public class FitnessActivity extends AppCompatActivity {
    private TextView activityGoalTextView;
    private TextView timerTextView;
    private Button startStopButton;
    private Button saveActivityButton;
    private EditText stepsEditText;
    private Button saveStepsButton;

    private Handler timerHandler = new Handler();
    private boolean stopwatchRunning = false;
    private int seconds = 0;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);

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

        activityGoalTextView = findViewById(R.id.activityGoalTextView);
        timerTextView = findViewById(R.id.timerTextView);

        startStopButton = findViewById(R.id.startStopButton);
        saveActivityButton = findViewById(R.id.saveActivityButton);

        stepsEditText = findViewById(R.id.stepsEditText);
        saveStepsButton = findViewById(R.id.saveStepsButton);



        Intent wellnessIntent = getIntent();
        Bundle extras = wellnessIntent.getExtras();
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
            String[] brokenDownTime = time.split(":");
            int[] times = new int[3];
            for (int i = 0; i < brokenDownTime.length; i++) {
                times[i] = Integer.parseInt(brokenDownTime[i]);
                System.out.println(times[i]);
            }
            // PASS THE TIMES ARRAY TO VIEWMODEL!!!!
            Toast.makeText(FitnessActivity.this,
                    "Activity logged!",
                    Toast.LENGTH_SHORT).show();
            stepsEditText.setText("");
            timerTextView.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
        });

        saveStepsButton.setOnClickListener(v -> {
            String steps = ValueExtractor.extract(stepsEditText);
            if (!InputValidator.isValidInputWithInteger(steps)) {
                Toast.makeText(FitnessActivity.this,
                        "Please enter a valid step count!",
                        Toast.LENGTH_SHORT).show();
            } else {
                int stepNum = Integer.parseInt(steps);
                // PASS stepNum  TO VIEWMODEL!!!!
                Toast.makeText(FitnessActivity.this,
                        "Steps tracked!",
                        Toast.LENGTH_SHORT).show();
                stepsEditText.setText("");
            }
        });


    }


    /**
     * DO NOT MODIFY!
     */
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


    /**
     DO NOT MODIFY!
     */
    private String formatTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}

