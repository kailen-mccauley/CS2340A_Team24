package com.example.greenplate.views;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.CheckBox;

import com.example.greenplate.viewmodels.RecipeActivityViewModel;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingActivity extends AppCompatActivity {

    private ShoppingListActivityViewModel shoppingListActivityViewModel;
    private Bundle extras;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private TextView buildTextView(int weight, String text) {
        TextView textView = new TextView(ShoppingActivity.this);
        LinearLayout.LayoutParams ingredientNameParams
                = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(ingredientNameParams);
        textView.setText(text);
        textView.setTextSize(22);
        textView.setTextColor(getResources().getColor(R.color.magenta));
        return textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);

        LinearLayout scrollable = findViewById(R.id.scrollLay);

        shoppingListActivityViewModel = ShoppingListActivityViewModel.getInstance();

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

        shoppingListActivityViewModel.fetchShoppingListItems(new ShoppingListActivityViewModel.ShoppingItemsListener() {
            @Override
            public void onShoppingItemsReceived(Map<String, Integer> existingItems) {
                Map<String, Integer> allIngredients = new HashMap<>(existingItems);
                Log.d("Shopping", "All Ingredients: "+allIngredients);
                for (Map.Entry<String, Integer> entry : allIngredients.entrySet()) {
                    String ingredient = entry.getKey();
                    int quancheck = entry.getValue();
                    Log.d("ShoppingActivity", "Ingredient: " + ingredient + ", Quantity: " + quancheck);
                    String quantity = String.valueOf(quancheck);
                    LinearLayout ingredientLayout = new LinearLayout(ShoppingActivity.this);
                    ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView ingredientNameTextView = buildTextView(3, ingredient);
                    TextView quantityTextView = buildTextView(2, quantity);
                    CheckBox checkBox = new CheckBox(ShoppingActivity.this);
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.magenta)));

                    ingredientLayout.addView(ingredientNameTextView);
                    ingredientLayout.addView(quantityTextView);
                    ingredientLayout.addView(checkBox);

                    scrollable.addView(ingredientLayout);
                    Log.d("ShoppingActivity", "Ingredient: " + ingredient + ", Quantity: " + quantity);
                }
            }
        });
    }
}
