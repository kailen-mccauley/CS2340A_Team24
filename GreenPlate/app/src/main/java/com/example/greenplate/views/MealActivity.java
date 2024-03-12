package com.example.greenplate.views;

import com.example.greenplate.models.User;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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
import com.google.firebase.auth.FirebaseAuth;

public class MealActivity extends AppCompatActivity {
    private MealActivityViewModel viewModel;
    private PersonalActivityViewModel personalViewModel; // Declare personalViewModel variable


    private FirebaseAuth mAuth;

    private EditText mealNameEditText;
    private EditText caloriesEditText;
    private Button btnSubmitMeal;
    private Button btnDailyCalorieIntake;
    private Button btnDailyGoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientsButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        TextView heightTextView = findViewById(R.id.heightTextView);
        TextView weightTextView = findViewById(R.id.weightTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView goalTextView = findViewById(R.id.goalTextView);
        TextView intakeTextView = findViewById(R.id.intakeTextView);


        mAuth = FirebaseAuth.getInstance();
        viewModel = MealActivityViewModel.getInstance();

        mealNameEditText = findViewById(R.id.mealNameEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        btnSubmitMeal = findViewById(R.id.btn_submit_meal);
        btnDailyCalorieIntake = findViewById(R.id.btn_daily_calorie_intake);
        btnDailyGoal = findViewById(R.id.btn_daily_goal);

        RelativeLayout parentLayout = findViewById(R.id.activity_input_meal);

        viewModel = MealActivityViewModel.getInstance();
        personalViewModel = PersonalActivityViewModel.getInstance();

        // Call getUserInfo to retrieve user information
        personalViewModel.getUserInfo(new PersonalActivityViewModel.UserInfoCallback() {
            @Override
            public void onUserInfoReceived(User user) {
                if (user != null) {
                    heightTextView.setText(user.getUserHeight());
                    weightTextView.setText(user.getUserWeight());
                    genderTextView.setText(user.getUserGender().equals("M") ? "Male" : "Female");
                    goalTextView.setText(String.valueOf(user.getCalorieGoal()));
                } else {
                    Toast.makeText(MealActivity.this,
                            "User information not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getDailyCalorieIntake(new MealActivityViewModel.DailyCalorieIntakeCallback() {
            @Override
            public void onDailyCalorieIntakeReceived(int totalCalories) {
                intakeTextView.setText(String.valueOf(totalCalories));
            }
        });

        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        toRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        toIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, IngredientsActivity.class);
                startActivity(intent);
            }
        });

        toShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });

        toPersonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        btnSubmitMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealName = mealNameEditText.getText().toString();
                String calories = caloriesEditText.getText().toString();
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
                viewModel.storeMeal(mealName, String.valueOf(calories));
                Toast.makeText(MealActivity.this,
                        "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                mealNameEditText.setText("");
                caloriesEditText.setText("");
                hideKeyboard();
                viewModel.getDailyCalorieIntake(new MealActivityViewModel.DailyCalorieIntakeCallback() {
                    @Override
                    public void onDailyCalorieIntakeReceived(int totalCalories) {
                        intakeTextView.setText(String.valueOf(totalCalories));
                    }
                });
            }
        });
        btnDailyGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, DataVisOne.class);
                startActivity(intent);
            }
        });
        btnDailyCalorieIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, DataVisTwo.class);
                startActivity(intent);
            }
        });
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide keyboard
                hideKeyboard();
                return false;
            }
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
