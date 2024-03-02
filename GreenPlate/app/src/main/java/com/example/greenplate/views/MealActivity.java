package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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
                // Retrieve values from input fields
                String mealName = mealNameEditText.getText().toString();
                String calories = caloriesEditText.getText().toString();

                // Call storeUserInformation function from viewModel
                viewModel.storeMeal(mealName, calories);

                // Optional: You can also add validation here before calling the function
            }
        });

    }
}
