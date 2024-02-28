package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;

public class PersonalActivity extends AppCompatActivity {

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

    }
}
