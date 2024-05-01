package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.utilites.InputFormatter;
import com.example.greenplate.utilites.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.utilites.ValueExtractor;
import com.example.greenplate.models.Ingredient;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class IngredientsActivity extends AppCompatActivity {

    private IngredientsActivityViewModel ingredientsViewModel;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }

    private void createScrollable(Map<String, Integer> ingredientsMap) {
        LinearLayout scrollableLayout = findViewById(R.id.linearIngred);
        scrollableLayout.removeAllViews();

        for (Map.Entry<String, Integer> entry : ingredientsMap.entrySet()) {
            String ingredientName = InputFormatter.capitalize(entry.getKey());
            Integer quantity = entry.getValue();

            LinearLayout ingredientLayout = new LinearLayout(IngredientsActivity.this);
            ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView ingredientNameTextView = buildTextView(3, ingredientName);
            TextView quantityTextView = buildTextView(2, String.valueOf(quantity));

            ingredientLayout.addView(ingredientNameTextView);
            ingredientLayout.addView(quantityTextView);

            scrollableLayout.addView(ingredientLayout);
        }
    }

    private TextView buildTextView(int weight, String text) {
        TextView textView = new TextView(IngredientsActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                weight
        );
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(22);
        textView.setText(text);
        return textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);

        ingredientsViewModel = IngredientsActivityViewModel.getInstance();
        ingredientsViewModel.getIngredQuanKVP(ingredientsMap -> createScrollable(ingredientsMap));

        Intent intentHome = new Intent(IngredientsActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentMeal = new Intent(IngredientsActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentRecipe = new Intent(IngredientsActivity.this, RecipeActivity.class);
        makeNavigationBar(toRecipeButton, intentRecipe);
        Intent intentShopping = new Intent(IngredientsActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentPersonal = new Intent(IngredientsActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentFitness = new Intent(IngredientsActivity.this, FitnessActivity.class);
        makeNavigationBar(toFitnessButton, intentFitness);


    }
}