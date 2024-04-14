package com.example.greenplate.views;

import com.example.greenplate.ValueExtractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

public class MealActivity extends AppCompatActivity {
    private MealActivityViewModel viewModel;
    private PersonalActivityViewModel personalViewModel;
    private EditText mealNameEditText;
    private EditText caloriesEditText;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        TextView heightTextView = findViewById(R.id.heightTextView);
        TextView weightTextView = findViewById(R.id.weightTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView goalTextView = findViewById(R.id.goalTextView);
        TextView intakeTextView = findViewById(R.id.intakeTextView);

        viewModel = MealActivityViewModel.getInstance();

        mealNameEditText = findViewById(R.id.mealNameEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        Button btnSubmitMeal = findViewById(R.id.btn_submit_meal);
        Button btnDailyCalorieIntake = findViewById(R.id.btn_daily_calorie_intake);
        Button btnDailyGoal = findViewById(R.id.btn_daily_goal);
        RelativeLayout parentLayout = findViewById(R.id.activity_input_meal);

        personalViewModel = PersonalActivityViewModel.getInstance();

        // Call getUserInfo to retrieve user information
        personalViewModel.getUserInfo(user ->  {
            if (user != null) {
                heightTextView.setText(user.getUserHeight());
                weightTextView.setText(user.getUserWeight());
                genderTextView.setText(user.getUserGender().equals("M") ? "Male" : "Female");
                goalTextView.setText(String.valueOf(user.getCalorieGoal()));
            } else {
                Toast.makeText(MealActivity.this,
                        "User information not available", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getDailyCalorieIntake(totalCalories ->
                intakeTextView.setText(String.valueOf(totalCalories)));

        Intent intentHome = new Intent(MealActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentRecipe = new Intent(MealActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentShopping = new Intent(MealActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentPersonal = new Intent(MealActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentIngredient = new Intent(MealActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);

        btnSubmitMeal.setOnClickListener(v ->  {
            String mealName = ValueExtractor.extract(mealNameEditText);
            String calories = ValueExtractor.extract(caloriesEditText);
            if (!InputValidator.isValidInputWithSpacesBetween(mealName)) {
                Toast.makeText(MealActivity.this,
                        "Please enter a valid meal name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(calories)) {
                Toast.makeText(MealActivity.this,
                        "Please enter a valid integer value for calories!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.storeMeal(mealName, calories);
            Toast.makeText(MealActivity.this,
                    "Submitted Successfully!", Toast.LENGTH_SHORT).show();
            mealNameEditText.setText("");
            caloriesEditText.setText("");
            hideKeyboard();
            viewModel.getDailyCalorieIntake(totalCalories ->
                    intakeTextView.setText(String.valueOf(totalCalories)));
        });
        btnDailyGoal.setOnClickListener(v ->  {
            if (personalViewModel.isValidUser()) {
                Intent intent = new Intent(MealActivity.this, DataVisOne.class);
                startActivity(intent);
            } else {
                Toast.makeText(MealActivity.this,
                        "Enter personal info before seeing visualizations!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btnDailyCalorieIntake.setOnClickListener(v ->  {
            if (personalViewModel.isValidUser()) {
                Intent intent = new Intent(MealActivity.this, DataVisTwo.class);
                startActivity(intent);
            } else {
                Toast.makeText(MealActivity.this,
                        "Enter personal info before seeing visualizations!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        parentLayout.setOnTouchListener((v, event) -> {
            // Hide keyboard
            hideKeyboard();
            return false;
        });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
