package com.example.greenplate.views;
import com.example.greenplate.viewmodels.HomeViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;

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

        Button metGoal = findViewById(R.id.completeButton);

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

        // metGoal.setOnClickListener(); Look at implementation in other screens for buttons if you get lost

        logFitnessStreak();
    }

    private void logFitnessStreak() {
        int currentStreak = viewModel.getFitnessStreak();
        Log.d("Fitness Streak", "Current fitness streak: " + currentStreak);
    }
}
