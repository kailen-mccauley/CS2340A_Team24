package com.example.greenplate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);

        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this,
                        HomeActivity.class); //CREATING INTENT
                startActivity(intent);
            }
        });

        toMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this,
                        MealActivity.class); //CREATING INTENT
                startActivity(intent);
            }
        });

        toRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this,
                        RecipeActivity.class); //CREATING INTENT
                startActivity(intent);
            }
        });

        toShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this,
                        ShoppingActivity.class); //CREATING INTENT
                startActivity(intent);
            }
        });

    }
}
