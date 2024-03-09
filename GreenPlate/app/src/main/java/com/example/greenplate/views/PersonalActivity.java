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
import com.example.greenplate.viewmodels.LoginViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class PersonalActivity extends AppCompatActivity {

    private PersonalActivityViewModel viewModel;

    private FirebaseAuth mAuth;

    private EditText weightEditText;
    private EditText heightEditText;
    private EditText genderEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientsButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toMealButton = findViewById(R.id.btn_meal);

        mAuth = FirebaseAuth.getInstance();
        viewModel = PersonalActivityViewModel.getInstance();

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        genderEditText = findViewById(R.id.genderEditText);
        submitButton = findViewById(R.id.btn_submit_personal_info);

        RelativeLayout parentLayout = findViewById(R.id.activity_personal_info);

        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        toRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, RecipeActivity.class);
                startActivity(intent);
            }
        });

        toIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, IngredientsActivity.class);
                startActivity(intent);
            }
        });

        toShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });

        toMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this, MealActivity.class);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from input fields
                String weight = weightEditText.getText().toString();
                String height = heightEditText.getText().toString();
                String gender = genderEditText.getText().toString();
                if (InputValidator.isValidInput(weight)
                        && InputValidator.isValidInput(height)
                        && InputValidator.isValidInput(gender)) {
                    try {
                        int heightValue = Integer.parseInt(height);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(PersonalActivity.this, "Please enter a valid integer value for height (in cm)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int weightValue = Integer.parseInt(weight);
                    } catch (NumberFormatException nfe) {
                        Toast.makeText(PersonalActivity.this, "Please enter a valid integer value for weight (in kg)", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!gender.equals("M") && !gender.equals("F")) {
                        Toast.makeText(PersonalActivity.this, "Please enter 'M' for male or 'F' for female", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    viewModel.storePersonalInfo(height, weight, gender);

                    Toast.makeText(PersonalActivity.this, "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    weightEditText.setText("");
                    heightEditText.setText("");
                    genderEditText.setText("");
                    hideKeyboard();
                } else {
                    Toast.makeText(PersonalActivity.this, "Field is filled out incorrectly!", Toast.LENGTH_SHORT).show();
                }
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
