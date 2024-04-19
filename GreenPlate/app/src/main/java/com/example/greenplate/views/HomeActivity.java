package com.example.greenplate.views;
import com.example.greenplate.decorators.ActivityDecorator;
import com.example.greenplate.decorators.BasicHomeScreenElement;
import com.example.greenplate.decorators.FitnessDecorator;
import com.example.greenplate.decorators.HomeScreenElement;
import com.example.greenplate.decorators.TimeDecorator;
import com.example.greenplate.viewmodels.HomeViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

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

        Button toLogoutButton = findViewById(R.id.logoutButton);

        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);

        CheckBox loadStreak = findViewById(R.id.streakButton);
        CheckBox loadSteps = findViewById(R.id.stepsButton);
        CheckBox loadTime = findViewById(R.id.timeButton);
        CheckBox loadActivity = findViewById(R.id.activButton);

        TextView timeSnip = findViewById(R.id.timeField);
        TextView activitySnip = findViewById(R.id.activityField);
        TextView stepsSnip = findViewById(R.id.stepsField);
        TextView streakSnip = findViewById(R.id.streakField);

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

        loadStreak.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userUid = user.getUid();
                    HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                    HomeScreenElement decoratedElement
                            = new FitnessDecorator(baseElement, this, userUid);
                    decoratedElement.display();
                }
            } else {
                streakSnip.setText("");
            }
        }));

        loadSteps.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userUid = user.getUid();
                    HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                    HomeScreenElement decoratedElement = new FitnessDecorator.StepsDecorator(
                            baseElement, this, userUid);
                    decoratedElement.display();
                }
            } else {
                stepsSnip.setText("");
            }
        }));

        loadTime.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            HomeScreenElement baseElement = new BasicHomeScreenElement(this);
            HomeScreenElement decoratedElement = new TimeDecorator(baseElement, this);
            if (isChecked) {
                timeSnip.setVisibility(View.VISIBLE);
                decoratedElement.display();
            } else {
                decoratedElement.display();
                timeSnip.setVisibility(View.INVISIBLE);
            }
        }));

        loadActivity.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userUid = user.getUid();
                    HomeScreenElement baseElement = new BasicHomeScreenElement(this);
                    HomeScreenElement decoratedElement
                            = new ActivityDecorator(baseElement, this, userUid);
                    decoratedElement.display();
                }
            } else {
                activitySnip.setText("");
            }
        });

        toLogoutButton.setOnClickListener(v -> {
//            viewModel.logout(() -> {
//
//            });
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}
