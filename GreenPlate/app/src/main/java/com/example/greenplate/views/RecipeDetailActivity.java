package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

import java.util.Map;

public class RecipeDetailActivity extends AppCompatActivity {
    private RecipeActivityViewModel recipeViewModel;
    private TextView title;
    private String recipeID;
    private Boolean canMake;

    private TextView buildTextView(int weight, String text) {
        TextView textView = new TextView(RecipeDetailActivity.this);
        LinearLayout.LayoutParams ingredientNameParams
                = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(ingredientNameParams);
        textView.setText(text);
        textView.setTextSize(22);
        textView.setTextColor(getResources().getColor(R.color.pennBlue));
        return textView;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        Button toRecipeScreen = findViewById(R.id.btn_to_recipe_screen);
        Button toCookOrShop = findViewById(R.id.btn_to_cook_or_shop);
        RelativeLayout parentLayout = findViewById(R.id.activity_input_ingredients);
        title = findViewById(R.id.recipeDetailsTextView);
        recipeViewModel = RecipeActivityViewModel.getInstance();
        if (InputValidator.isValidRecipeDetailIntent(getIntent())) {
            recipeID = getIntent().getStringExtra("recipeID");
            title.setText(getIntent().getStringExtra("recipeName"));
            canMake = getIntent().getBooleanExtra("canMake", false);
        }
        if (!canMake) {
            toCookOrShop.setText("Add to\nShopping List");
        }
        recipeViewModel.getRecipeDetails(recipeID, new
                RecipeActivityViewModel.RecipeDetailsListener() {
            @Override
            public void onRecipeDetailsReceived(Recipe recipe) {
                LinearLayout scrollable = findViewById(R.id.scrollableLay);
                for (Map.Entry<String, Integer> entry : recipe.
                        getIngredients().entrySet()) {
                    String ingredientName = entry.getKey();
                    String quantity = entry.getValue().toString();
                    LinearLayout ingredientLayout
                                    = new LinearLayout(RecipeDetailActivity.this);
                    ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView ingredientNameTextView = buildTextView(3, ingredientName);
                    TextView quantityTextView = buildTextView(2, quantity);
                    ingredientLayout.addView(ingredientNameTextView);
                    ingredientLayout.addView(quantityTextView);

                    scrollable.addView(ingredientLayout);
                }
            }
            @Override
            public void onRecipeDetailsError(String errorMessage) {
            }
        });

        toRecipeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetailActivity.this,
                        RecipeActivity.class);
                startActivity(intent);
            }
        });
        toCookOrShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeViewModel.getRecipeDetails(recipeID,
                        new RecipeActivityViewModel.RecipeDetailsListener() {
                            @Override
                            public void onRecipeDetailsReceived(Recipe recipe) {
                                Map<String, Integer> ingredients = recipe.getIngredients();
                                IngredientsActivityViewModel ingredientsVM =
                                        IngredientsActivityViewModel.getInstance();
                                if (canMake) {
                                    ingredientsVM.getCaloriesForRecipe(ingredients, new
                                            IngredientsActivityViewModel.CaloriesListener() {
                                        @Override
                                        public void onCaloriesReceived(int calories) {
                                            MealActivityViewModel mealVM =
                                                    MealActivityViewModel.getInstance();
                                            mealVM.storeMeal(recipe.getRecipeName(),
                                                    String.valueOf(calories));
                                            ingredientsVM.decreaseIngredQuantByRecipe(ingredients);
                                        }
                                    });
                                } else {
                                    ShoppingListActivityViewModel shoppingVM =
                                            ShoppingListActivityViewModel.getInstance();
                                    for (String ingredient : ingredients.keySet()) {
                                        ingredientsVM.getIngredTree(new IngredientsActivityViewModel
                                                .IngredientTreeMapListener() {
                                            @Override
                                            public void onIngredTreeReceive(Map<String,
                                                    Ingredient> ingredientMap) {
                                                Ingredient pantryIngredient =
                                                        ingredientMap.get(ingredient);
                                                int quantity = ingredients.get(ingredient);
                                                if (pantryIngredient != null
                                                    && pantryIngredient.getQuantity() < quantity) {
                                                    quantity -= pantryIngredient.getQuantity();
                                                    shoppingVM.storeShoppingListItem(ingredient,
                                                            quantity);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onRecipeDetailsError(String errorMessage) {
                            }
                        });
                Intent intent = new Intent(RecipeDetailActivity.this,
                        RecipeActivity.class);
                startActivity(intent);
                String message = canMake ? " was cooked!" : " ingredients added to shopping list!";
                Toast.makeText(RecipeDetailActivity.this,
                        title.getText() + message, Toast.LENGTH_SHORT).show();
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

