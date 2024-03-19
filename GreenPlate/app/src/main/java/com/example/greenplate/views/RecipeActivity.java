package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    private EditText recipeNameEditText;
    private EditText ingredientListEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        RelativeLayout parentLayout = findViewById(R.id.activity_recipe);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toIngredientsButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        Switch sortSwitch = findViewById(R.id.sortingSwitch);
        Spinner recipesSpinner = findViewById(R.id.recipesSpinner);
        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        ingredientListEditText = findViewById(R.id.recipeIngredientsEditText);
        Button submitRecipe = findViewById(R.id.btn_submit_recipe);


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
        sortSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // SORTING BASED ON Available INGREDIENTS

                } else {
                    // SORTING ALPHABETICALLY
                }
            }
        });

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = recipeNameEditText.getText().toString();
                String ingredientList = ingredientListEditText.getText().toString();
                if (!InputValidator.isValidInputWithSpacesBetween(recipeName)) {
                    Toast.makeText(RecipeActivity.this,
                            "Please input a name for your recipe!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!InputValidator.isValidInputWithSpacesBetween(ingredientList)) {
                    Toast.makeText(RecipeActivity.this,
                            "Please input the ingredient list for your recipe!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] ingredients = ingredientList.split(",");
                for (String ingredient: ingredients) {
                    Log.d("ingredient", ingredient);
                }
            }
        });
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Hide keyboard
                hideKeyboard();
                return false;
            }
        });
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
