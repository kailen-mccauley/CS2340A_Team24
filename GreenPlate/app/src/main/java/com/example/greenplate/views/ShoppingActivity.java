package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_shopping_list);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);

        Intent intentHome = new Intent(ShoppingActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentMeal = new Intent(ShoppingActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentRecipe = new Intent(ShoppingActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentPersonal = new Intent(ShoppingActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentIngredient = new Intent(ShoppingActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);
    }
}
