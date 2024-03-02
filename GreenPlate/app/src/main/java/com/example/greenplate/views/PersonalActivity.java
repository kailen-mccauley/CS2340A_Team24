package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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

                // Call storeUserInformation function from viewModel
                viewModel.storePersonalInfo(height, weight, gender);

                // Optional: You can also add validation here before calling the function
            }
        });


    }

}
