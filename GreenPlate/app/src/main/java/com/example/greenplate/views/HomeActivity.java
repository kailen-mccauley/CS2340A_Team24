package com.example.greenplate.views;
import com.example.greenplate.decorators.ActivityDecorator;
import com.example.greenplate.decorators.BasicHomeScreenElement;
import com.example.greenplate.decorators.FitnessDecorator;
import com.example.greenplate.decorators.HomeScreenElement;
import com.example.greenplate.decorators.StepsDecorator;
import com.example.greenplate.decorators.TimeDecorator;
import com.example.greenplate.viewmodels.HomeViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


import com.example.greenplate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
        Button loadTime = findViewById(R.id.timeButton);
        Button loadActivity = findViewById(R.id.activityButton);


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

        loadTime.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userUid = user.getUid();
                HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                HomeScreenElement decoratedElement = new TimeDecorator(baseElement, this);
                decoratedElement.display();
            }
        });

        loadActivity.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userUid = user.getUid();
                HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                HomeScreenElement decoratedElement = new ActivityDecorator(baseElement, this, userUid);
                decoratedElement.display();
            }
        });

    }
}
