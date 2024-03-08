package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private FirebaseAuth mAuth;

    private EditText mealNameEditText;
    private EditText caloriesEditText;
    private Button btn_submit_meal;
    private Button btn_daily_calorie_intake;


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

        mAuth = FirebaseAuth.getInstance();
        viewModel = MealActivityViewModel.getInstance();

        mealNameEditText = findViewById(R.id.mealNameEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        btn_submit_meal = findViewById(R.id.btn_submit_meal);
        btn_daily_calorie_intake = findViewById(R.id.btn_daily_calorie_intake);

        RelativeLayout parentLayout = findViewById(R.id.activity_input_meal);


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

        btn_submit_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealName = mealNameEditText.getText().toString();
                String calories = caloriesEditText.getText().toString();

                if (InputValidator.isValidInputWithSpacesBetween(mealName)
                        && InputValidator.isValidInput(calories)) {
                    try {
                        int calorieValue = Integer.parseInt(calories);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(MealActivity.this, "Please enter a valid integer value for calories", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    viewModel.storeMeal(mealName, calories);

                    Toast.makeText(MealActivity.this, "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    mealNameEditText.setText("");
                    caloriesEditText.setText("");
                    hideKeyboard();
                } else {
                    Toast.makeText(MealActivity.this, "A field you entered is invalid!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_daily_calorie_intake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealActivity.this, DataVisOne.class);
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
