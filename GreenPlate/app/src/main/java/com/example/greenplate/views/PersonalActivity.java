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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.ValueExtractor;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

public class PersonalActivity extends AppCompatActivity {

    private PersonalActivityViewModel viewModel;
    private EditText weightEditText;
    private EditText heightEditText;
    private EditText genderEditText;

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
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toMealButton = findViewById(R.id.btn_meal);

        viewModel = PersonalActivityViewModel.getInstance();

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        genderEditText = findViewById(R.id.genderEditText);
        Button submitButton = findViewById(R.id.btn_submit_personal_info);

        RelativeLayout parentLayout = findViewById(R.id.activity_personal_info);

        Intent intentHome = new Intent(PersonalActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentMeal = new Intent(PersonalActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentRecipe = new Intent(PersonalActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentShopping = new Intent(PersonalActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentIngredient = new Intent(PersonalActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = ValueExtractor.extract(weightEditText);
                String height = ValueExtractor.extract(heightEditText);
                String gender = ValueExtractor.extract(genderEditText);
                if (!InputValidator.isValidInputWithInteger(height)) {
                    Toast.makeText(PersonalActivity.this,
                            "Please enter a valid integer value for height (in cm)",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!InputValidator.isValidInputWithInteger(weight)) {
                    Toast.makeText(PersonalActivity.this,
                            "Please enter a valid integer value for weight (in kg)",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!InputValidator.isValidGender(gender)) {
                    Toast.makeText(PersonalActivity.this,
                            "Please enter 'M' for male or 'F' for female",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.storePersonalInfo(height, weight, gender);
                Toast.makeText(PersonalActivity.this,
                        "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                weightEditText.setText("");
                heightEditText.setText("");
                genderEditText.setText("");
                hideKeyboard();
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

