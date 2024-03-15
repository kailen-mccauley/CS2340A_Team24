package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toIngredientsButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        Button increaseQuantity = findViewById(R.id.btn_increase_quantity);
        Button decreaseQuantity = findViewById(R.id.btn_decrease_quantity);


        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Sugar");
        ingredients.add("Flour");
        ingredients.add("Butter");

        Spinner ingredientNameSpinner = findViewById(R.id.ingredientNameSpinner);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, ingredients);
        ingredientsAdapter.insert("Select ingredient", 0);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientNameSpinner.setAdapter(ingredientsAdapter);

        ArrayList<String> quantities = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            quantities.add(String.valueOf(i));
        }

        Spinner quantatiesSpinner = findViewById(R.id.quantitySpinner);
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, quantities);
        quantityAdapter.insert("Select quantity", 0);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantatiesSpinner.setAdapter(quantityAdapter);


        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        toMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, MealActivity.class);
                startActivity(intent);
            }
        });

        toIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, IngredientsActivity.class);
                startActivity(intent);
            }
        });

        toShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, ShoppingActivity.class);
                startActivity(intent);
            }
        });

        toPersonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
