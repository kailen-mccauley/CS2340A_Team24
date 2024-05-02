package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.greenplate.R;
import android.widget.Button;
import android.widget.Toast;

import com.example.greenplate.utilites.InputValidator;
import com.example.greenplate.viewmodels.FitnessActivityViewModel;
import com.example.greenplate.viewmodels.HomeViewModel;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

public class HomeActivity extends AppCompatActivity {
    private ProgressBar stepsProgressBar;
    private ProgressBar caloriesProgressBar;
    private ProgressBar fitnessProgressBar;
    private PersonalActivityViewModel personalVM;
    private MealActivityViewModel mealVM;
    private FitnessActivityViewModel fitnessVM;

    private HomeViewModel viewModel;
    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = HomeViewModel.getInstance(this.getApplicationContext());

        caloriesProgressBar = findViewById(R.id.calorie_goal_progress_bar);
        stepsProgressBar = findViewById(R.id.steps_goal_progress_bar);
        fitnessProgressBar = findViewById(R.id.fitness_goal_progress_bar);

        Button toLogoutButton = findViewById(R.id.logoutButton);

        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);

        CheckBox stepsCheckBox = findViewById(R.id.stepsCheckBox);
        stepsCheckBox.setChecked(true);
        CheckBox caloriesCheckBox = findViewById(R.id.caloriesCheckBox);
        caloriesCheckBox.setChecked(true);
        CheckBox fitnessCheckBox = findViewById(R.id.fitnessCheckBox);
        fitnessCheckBox.setChecked(true);

        Intent intentMeal = new Intent(HomeActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentRecipe = new Intent(HomeActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentShopping = new Intent(HomeActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentPersonal = new Intent(HomeActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentIngredient = new Intent(HomeActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);
        Intent intentFitness = new Intent(HomeActivity.this, FitnessActivity.class);
        makeNavigationBar(toFitnessButton, intentFitness);

        personalVM = PersonalActivityViewModel.getInstance();
        mealVM = MealActivityViewModel.getInstance();
        fitnessVM = FitnessActivityViewModel.getInstance();

        personalVM.getUserInfo(userInfoReceived -> {
            if(userInfoReceived != null && InputValidator.isValidUserInfo(userInfoReceived)) {
                mealVM.getDailyCalorieIntake(calorieIntakeReceived -> {
                    float calorieGoal = userInfoReceived.getCalorieGoal();
                    int progress = (int) (((float) calorieIntakeReceived / calorieGoal) * 100);
                    caloriesProgressBar.setProgress(progress);
                });
                fitnessVM.getDailySteps(stepsReceived -> {
                    float stepGoal = userInfoReceived.getStepGoal();
                    int progress = (int) (((float) stepsReceived / stepGoal) * 100);
                    stepsProgressBar.setProgress(progress);
                });
                fitnessVM.getDailyActivity(activityDurationReceived -> {
                    float workoutGoal = userInfoReceived.getWorkoutGoalInMinutes()* 60;
                    int progress = (int) (((float) activityDurationReceived.getSeconds() / workoutGoal) * 100);
                    fitnessProgressBar.setProgress(progress);
                });
            } else {
                Toast.makeText(HomeActivity.this,
                        "Please update your personal info to see progress rings!", Toast.LENGTH_SHORT).show();
                ConstraintLayout ringsAndCheckBoxes = findViewById(R.id.fitnessRingsAndCheckBoxes);
                ringsAndCheckBoxes.setVisibility(View.INVISIBLE);
            }
        });

        stepsCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                stepsProgressBar.setVisibility(View.VISIBLE);
            } else {
                stepsProgressBar.setVisibility(View.INVISIBLE);
            }
        }));
        caloriesCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                caloriesProgressBar.setVisibility(View.VISIBLE);
            } else {
                caloriesProgressBar.setVisibility(View.INVISIBLE);
            }
        }));
        fitnessCheckBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                fitnessProgressBar.setVisibility(View.VISIBLE);
            } else {
                fitnessProgressBar.setVisibility(View.INVISIBLE);
            }
        }));

        toLogoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
