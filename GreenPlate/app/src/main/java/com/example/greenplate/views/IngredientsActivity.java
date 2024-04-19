package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private TreeMap<String, Ingredient> ingredientsTreeMap;

    private void updateTreeMapAndSpinners(Map<String, Ingredient> ingredientMap,
                                          Spinner ingredientNameSpinner,
                                          Spinner quantitiesSpinner) {
        ingredientsTreeMap = new TreeMap<>(ingredientMap);
        ArrayList<String> ingredientNames = new ArrayList<>(ingredientsTreeMap.keySet());
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(IngredientsActivity.this,
                R.layout.spinner_item_layout_ingredients, ingredientNames);
        ingredientsAdapter.insert("Select ingredient", 0);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientNameSpinner.setAdapter(ingredientsAdapter);
        ingredientNameSpinner.setSelection(0);
        quantitiesSpinner.setSelection(0);
    }

    private void makeQuantitySpinner(Spinner quantitiesSpinner) {
        ArrayList<String> quantities = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            quantities.add(String.valueOf(i));
        }
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_layout_ingredients, quantities);
        quantityAdapter.insert("Select quantity", 0);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitiesSpinner.setAdapter(quantityAdapter);
    }
    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v -> startActivity(intent));
    }

    private void createScrollable(Map<String, Integer> ingredientsMap) {
        LinearLayout scrollableLayout = findViewById(R.id.linearIngred);
        scrollableLayout.removeAllViews();

        for (Map.Entry<String, Integer> entry : ingredientsMap.entrySet()) {
            String ingredientName = entry.getKey();
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

        Button toIngredientsForm = findViewById(R.id.btn_to_ingredients_form);
        TextView currQuantity = findViewById(R.id.currQuantityTextView);
        Button increaseQuantity = findViewById(R.id.btn_increase_quantity);
        Button decreaseQuantity = findViewById(R.id.btn_decrease_quantity);
        Spinner ingredientNameSpinner = findViewById(R.id.ingredientNameSpinner);
        Spinner quantitiesSpinner = findViewById(R.id.quantitySpinner);

        ingredientsViewModel = IngredientsActivityViewModel.getInstance();
        ingredientsViewModel.getIngredTree(ingredientMap ->
                updateTreeMapAndSpinners(ingredientMap, ingredientNameSpinner, quantitiesSpinner));
        makeQuantitySpinner(quantitiesSpinner);

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

        toIngredientsForm.setOnClickListener(v -> {
            Intent intent = new Intent(IngredientsActivity.this, IngredientsFormActivity.class);
            startActivity(intent);
        });
        increaseQuantity.setOnClickListener(v -> {
            String ingredientName = ValueExtractor.extract(ingredientNameSpinner);
            String quantity = ValueExtractor.extract(quantitiesSpinner);
            if (!InputValidator.isValidSpinnerItem(ingredientName)) {
                Toast.makeText(IngredientsActivity.this, "Please select a ingredient "
                        + "in your pantry!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidSpinnerItem(quantity)) {
                Toast.makeText(IngredientsActivity.this,
                        "Please select a quantity!", Toast.LENGTH_SHORT).show();
                return;
            }
            ingredientsViewModel.updateIngredQuanTree(ingredientName, Integer.parseInt(quantity),
                    ingredientMap -> {
                    updateTreeMapAndSpinners(ingredientMap, ingredientNameSpinner,
                            quantitiesSpinner);
                        ingredientsViewModel.getIngredQuanKVP(ingredientsMap
                                -> createScrollable(ingredientsMap));
                    Toast.makeText(IngredientsActivity.this, "Quantity of " + ingredientName
                            + " increased by " + quantity + "!", Toast.LENGTH_SHORT).show();
                });
        });
        decreaseQuantity.setOnClickListener(v ->  {
            String ingredientName = ValueExtractor.extract(ingredientNameSpinner);
            String quantity = ValueExtractor.extract(quantitiesSpinner);
            if (!InputValidator.isValidSpinnerItem(ingredientName)) {
                Toast.makeText(IngredientsActivity.this, "Please select a ingredient "
                        + "in your pantry!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidSpinnerItem(quantity)) {
                Toast.makeText(IngredientsActivity.this,
                        "Please select a quantity!", Toast.LENGTH_SHORT).show();
                return;
            }
            int decrementQuantity = Integer.parseInt(quantity);
            int currentQuantity = ingredientsTreeMap.get(ingredientName).getQuantity();
            if (!InputValidator.isValidQuantityDecrease(decrementQuantity, currentQuantity)) {
                Toast.makeText(IngredientsActivity.this,
                        "Quantity of " + ingredientName + " can only be decreased by a max of "
                                + currentQuantity + "!", Toast.LENGTH_SHORT).show();
                return;
            }
            ingredientsViewModel.decreaseIngrQuanTree(ingredientName, Integer.parseInt(quantity),
                    ingredientMap -> {
                        updateTreeMapAndSpinners(ingredientMap,
                                ingredientNameSpinner, quantitiesSpinner);
                        ingredientsViewModel.getIngredQuanKVP(ingredientsMap -> {
                            createScrollable(ingredientsMap);
                        });
                    },
                    IngredientsActivity.this
            );
        });
        ingredientNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                String selectedRecipe = parentView.getItemAtPosition(position).toString();
                if (InputValidator.isValidSpinnerItem(selectedRecipe)) {
                    int quantity = ingredientsTreeMap.get(selectedRecipe).getQuantity();
                    currQuantity.setText(String.valueOf(quantity));
                } else {
                    currQuantity.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
}
