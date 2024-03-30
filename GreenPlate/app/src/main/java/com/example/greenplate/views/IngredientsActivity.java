package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.models.Ingredient;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class IngredientsActivity extends AppCompatActivity {

    private IngredientsActivityViewModel IngredientsViewModel;
    private TreeMap<String, Ingredient> ingredientsTreeMap;

    private void updateTreeMapAndSpinners(Map<String, Ingredient> ingredientMap, Spinner ingredientNameSpinner) {
        // Update the ingredients TreeMap
        ingredientsTreeMap = new TreeMap<>(ingredientMap);
        // Update the spinners with the new TreeMap data
        ArrayList<String> ingredientNames = new ArrayList<>(ingredientsTreeMap.keySet());
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(IngredientsActivity.this, R.layout.spinner_item_layout_ingredients, ingredientNames);
        ingredientsAdapter.insert("Select ingredient", 0);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientNameSpinner.setAdapter(ingredientsAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toRecipeButton = findViewById(R.id.btn_recipe);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        Button toIngredientsForm = findViewById(R.id.btn_to_ingredients_form);

        TextView currQuantity = findViewById(R.id.currQuantityTextView);
        Button increaseQuantity = findViewById(R.id.btn_increase_quantity);
        Button decreaseQuantity = findViewById(R.id.btn_decrease_quantity);
        Spinner ingredientNameSpinner = findViewById(R.id.ingredientNameSpinner);

        IngredientsViewModel = IngredientsActivityViewModel.getInstance();


        //Here is an example of how to get the Ingredients Tree Map

        IngredientsViewModel.getIngredientsTreeMap(new IngredientsActivityViewModel.IngredientTreeMapListener() {
            @Override
            public void onIngredientsTreeMapReceived(Map<String, Ingredient> ingredientMap) {
                updateTreeMapAndSpinners(ingredientMap, ingredientNameSpinner);
            }
        });

        ArrayList<String> quantities = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            quantities.add(String.valueOf(i));
        }

        Spinner quantatiesSpinner = findViewById(R.id.quantitySpinner);
        ArrayAdapter<String> quantityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout_ingredients, quantities);
        quantityAdapter.insert("Select quantity", 0);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantatiesSpinner.setAdapter(quantityAdapter);

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
        toPersonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });
        toIngredientsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IngredientsActivity.this, IngredientsFormActivity.class);
                startActivity(intent);
            }
        });

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = ingredientNameSpinner.getSelectedItem().toString();
                String quantity = quantatiesSpinner.getSelectedItem().toString();
                if (!InputValidator.isValidSpinnerItem(ingredientName)) {
                    Toast.makeText(IngredientsActivity.this,
                            "Please select a ingredient in your pantry!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!InputValidator.isValidSpinnerItem(quantity)) {
                    Toast.makeText(IngredientsActivity.this,
                            "Please select a quantity!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                IngredientsViewModel.updateIngredientQuantityAndTreeMap(ingredientName, Integer.valueOf(quantity), new IngredientsActivityViewModel.IngredientTreeMapListener() {
                    @Override
                    public void onIngredientsTreeMapReceived(Map<String, Ingredient> ingredientMap) {
                        updateTreeMapAndSpinners(ingredientMap, ingredientNameSpinner);
                        ingredientNameSpinner.setSelection(0);
                        quantatiesSpinner.setSelection(0);
                        Toast.makeText(IngredientsActivity.this,
                                "Quantity of " + ingredientName + " increased by " + quantity + "!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = ingredientNameSpinner.getSelectedItem().toString();
                String quantity = quantatiesSpinner.getSelectedItem().toString();
                if (!InputValidator.isValidSpinnerItem(ingredientName)) {
                    Toast.makeText(IngredientsActivity.this,
                            "Please select a ingredient in your pantry!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!InputValidator.isValidSpinnerItem(quantity)) {
                    Toast.makeText(IngredientsActivity.this,
                            "Please select a quantity!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int decreaseQuantity = Integer.valueOf(quantity);
                int currentQuantity = ingredientsTreeMap.get(ingredientName).getQuantity();
                if (!InputValidator.isValidQuantityDecrease(decreaseQuantity, currentQuantity)) {
                    Toast.makeText(IngredientsActivity.this,
                            "Quantity of " + ingredientName
                                    + " can only be decreased by a max of "
                                    + currentQuantity + "!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                IngredientsViewModel.decreaseIngredientQuantityAndTreeMap(ingredientName,Integer.valueOf(quantity),
                        new IngredientsActivityViewModel.IngredientTreeMapListener() {
                            @Override
                            public void onIngredientsTreeMapReceived(Map<String, Ingredient> ingredientMap) {
                                updateTreeMapAndSpinners(ingredientMap, ingredientNameSpinner);
                                ingredientNameSpinner.setSelection(0);
                                quantatiesSpinner.setSelection(0);
                            }
                        }, IngredientsActivity.this

                );
            }
        });
        ingredientNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perform actions when an item is selected
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
