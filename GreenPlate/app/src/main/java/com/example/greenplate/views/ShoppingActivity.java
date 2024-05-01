package com.example.greenplate.views;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.core.content.ContextCompat;

import com.example.greenplate.observers.ShoppingFormObserver;
import com.example.greenplate.utilites.InputFormatter;
import com.example.greenplate.utilites.ValueExtractor;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;

import java.util.ArrayList;
import java.util.Map;

public class ShoppingActivity extends AppCompatActivity implements ShoppingFormObserver {

    private ShoppingListActivityViewModel viewModel;
    private LinearLayout scrollable;
    private Bundle extras;

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }

    private TextView buildTextView(int weight, String text) {
        TextView textView = new TextView(ShoppingActivity.this);
        LinearLayout.LayoutParams ingredientNameParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(ingredientNameParams);
        textView.setText(text);
        textView.setTextSize(22);
        textView.setTextColor(ContextCompat.getColor(this, R.color.darkBrown));
        return textView;
    }

    private void populateShoppingList(Map<String, Integer> items, LinearLayout scrollable) {
        scrollable.removeAllViews();

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String ingredient = InputFormatter.capitalize(entry.getKey());
            int quantityCheck = entry.getValue();
            String quantity = String.valueOf(quantityCheck);
            LinearLayout ingredientLayout = new LinearLayout(ShoppingActivity.this);
            ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView ingredientNameTextView = buildTextView(3, ingredient);

            TextView quantityTextView = buildTextView(1, quantity);
            CheckBox checkBox = new CheckBox(ShoppingActivity.this);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(2,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            checkBox.setButtonTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.darkBrown)));

            ImageButton minusButton = new ImageButton(ShoppingActivity.this);
            minusButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            minusButton.setImageResource(R.drawable.ic_minus_icon);
            minusButton.setBackgroundColor(Color.TRANSPARENT);
            minusButton.setOnClickListener(v ->  {
                int currentQuantity = Integer.parseInt(ValueExtractor.extract(quantityTextView));
                if (currentQuantity > 1) {
                    currentQuantity--;
                    quantityTextView.setText(String.valueOf(currentQuantity));
                    // shoppingListActivityViewModel.getCaloriesFromPantry();
                    viewModel.storeShoppingListItem(
                            ValueExtractor.extract(ingredientNameTextView).toLowerCase(),
                            -1, 5,
                            this::updateShoppingScrollable);
                } else {
                    viewModel.removeShoppingListItem(
                            ValueExtractor.extract(ingredientNameTextView),
                            this::updateShoppingScrollable);
                }

            });

            ImageButton addButton = new ImageButton(ShoppingActivity.this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            addButton.setImageResource(R.drawable.ic_add_icon);
            addButton.setBackgroundColor(Color.TRANSPARENT);
            addButton.setOnClickListener(v ->  {
                int currentQuantity = Integer.parseInt(ValueExtractor.extract(quantityTextView));
                currentQuantity++;
                quantityTextView.setText(String.valueOf(currentQuantity));
                viewModel.storeShoppingListItem(
                        ValueExtractor.extract(ingredientNameTextView).toLowerCase(),
                        1, 5,
                        this::updateShoppingScrollable);
            });

            ingredientLayout.addView(ingredientNameTextView);
            ingredientLayout.addView(minusButton);
            ingredientLayout.addView(quantityTextView);
            ingredientLayout.addView(addButton);
            ingredientLayout.addView(checkBox);

            scrollable.addView(ingredientLayout);
        }
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
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);
        Button buyItems = findViewById(R.id.btn_buy_items);
        Button toManualForm = findViewById(R.id.btn_add_ingredient);

        scrollable = findViewById(R.id.scrollLay);

        viewModel = ShoppingListActivityViewModel.getInstance();
        viewModel.addObserver(this);


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
        Intent intentFitness = new Intent(ShoppingActivity.this, FitnessActivity.class);
        makeNavigationBar(toFitnessButton, intentFitness);

        updateShoppingScrollable();

        toManualForm.setOnClickListener(v -> showAddShoppingIngredientPopup());

        buyItems.setOnClickListener(v ->  {
            ArrayList<String> toBuy = new ArrayList<>();
            int childCount = scrollable.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = scrollable.getChildAt(i);
                if (view instanceof LinearLayout) {
                    LinearLayout ingredientLayout = (LinearLayout) view;
                    CheckBox checkBox = (CheckBox) ingredientLayout.getChildAt(4);
                    if (checkBox.isChecked()) {
                        String ingredientName = ValueExtractor.extract(
                                (TextView) ingredientLayout.getChildAt(0));
                        toBuy.add(InputFormatter.lowercase(ingredientName));
                    }
                }
            }
            viewModel.buyItems(toBuy, this::updateShoppingScrollable);
        });
    }
    private void showAddShoppingIngredientPopup() {
        ShoppingFormPopupActivity dialog = new ShoppingFormPopupActivity();
        dialog.show(getSupportFragmentManager(), "RecipePopupDialogFragment");
    }
    @Override
    public void updateShoppingScrollable() {
        viewModel.fetchShoppingListItems(existingItems ->
                populateShoppingList(existingItems, scrollable));
    }
}
