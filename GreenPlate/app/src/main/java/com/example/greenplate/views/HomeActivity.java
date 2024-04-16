package com.example.greenplate.views;
import com.example.greenplate.viewmodels.HomeViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.TextView;
import com.example.greenplate.*;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.greenplate.FitnessActivityObserver;
import com.example.greenplate.FitnessActivityObserver;


import com.example.greenplate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.constraintlayout.widget.ConstraintLayout;


public class HomeActivity extends AppCompatActivity {

    private HomeViewModel viewModel;



    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewModel = HomeViewModel.getInstance(this.getApplicationContext());


        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);

        Button loadStreak = findViewById(R.id.streakButton);
        Button loadSteps = findViewById(R.id.stepsButton);


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

        loadStreak.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userUid = user.getUid();
                HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                HomeScreenElement decoratedElement = new FitnessDecorator(baseElement, this, userUid);
                decoratedElement.display();
            }
        });

        loadSteps.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userUid = user.getUid();
                HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                HomeScreenElement decoratedElement = new StepsDecorator(baseElement, this, userUid);
                decoratedElement.display();
            }
        });

    }
}
