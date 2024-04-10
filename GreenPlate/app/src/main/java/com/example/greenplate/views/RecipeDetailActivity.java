package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.greenplate.models.Meal;
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
        recipeViewModel.getRecipeDetails(recipeID, new RecipeActivityViewModel.RecipeDetailsListener() {
            @Override
            public void onRecipeDetailsReceived(Recipe recipe) {
                LinearLayout scrollable = findViewById(R.id.scrollableLay);
                for (Map.Entry<String, Integer> entry : recipe.
                        getIngredients().entrySet()) {
                    String ingredientName = entry.getKey();
                    int quantity = entry.getValue();
                    LinearLayout ingredientLayout
                            = new LinearLayout(RecipeDetailActivity.this);
                    ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView ingredientNameTextView = new TextView(RecipeDetailActivity.this);
                    LinearLayout.LayoutParams ingredientNameParams
                            = new LinearLayout.LayoutParams(
                                    0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            3
                    );
                    ingredientNameTextView.setLayoutParams(ingredientNameParams);
                    ingredientNameTextView.setText(ingredientName);
                    ingredientNameTextView.setTextSize(22);
                    ingredientNameTextView.setTextColor(getResources().getColor(R.color.pennBlue));

                    TextView quantityTextView = new TextView(RecipeDetailActivity.this);
                    LinearLayout.LayoutParams quantityParams
                            = new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            2
                    );
                    quantityParams.setMarginStart(50);
                    quantityTextView.setLayoutParams(quantityParams);
                    quantityTextView.setText(String.valueOf(quantity));
                    quantityTextView.setTextSize(22);
                    quantityTextView.setTextColor(getResources().getColor(R.color.pennBlue));

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

        // Potentially strategy pattern here?????
        // One strategy can be for if the user can make a recipe, we "cook" the meal.
        // Second strategy can be for if they cannot make a recipe, so we add ingredients to the shopping cart
        toCookOrShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeViewModel.getRecipeDetails(recipeID,
                        new RecipeActivityViewModel.RecipeDetailsListener() {
                            @Override
                            public void onRecipeDetailsReceived(Recipe recipe) {
                                Map<String, Integer> ingredients = recipe.getIngredients();
                                if (canMake) {
                                    IngredientsActivityViewModel ingredientsVM = IngredientsActivityViewModel.getInstance();
                                    ingredientsVM.getCaloriesForRecipe(ingredients, new IngredientsActivityViewModel.CaloriesListener() {
                                        @Override
                                        public void onCaloriesReceived(int calories) {
                                            MealActivityViewModel mealVM = MealActivityViewModel.getInstance();
                                            mealVM.storeMeal(recipe.getRecipeName(), String.valueOf(calories));
                                            ingredientsVM.decreaseIngredQuantByRecipe(ingredients);
                                        }
                                    });
                                } else {
                                    ShoppingListActivityViewModel shoppingVM = ShoppingListActivityViewModel.getInstance();
                                    for (String ingredient : ingredients.keySet()) {
                                        int quantity = ingredients.get(ingredient);
                                        shoppingVM.storeShoppingListItem(ingredient, quantity);
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

