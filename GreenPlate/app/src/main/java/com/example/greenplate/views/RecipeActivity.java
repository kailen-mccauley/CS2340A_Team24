package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Switch;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.observers.RecipeFormObserver;
import com.example.greenplate.utilites.InputFormatter;
import com.example.greenplate.utilites.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.utilites.ValueExtractor;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements RecipeFormObserver {
    private RecipeActivityViewModel viewModel;
    private EditText recipeNameEditText;
    private EditText ingredientListEditText;
    private Spinner recipesSpinner;
    private Switch sortSwitch;
    private ArrayList<Map<SpannableString, Recipe>> recipesList;

    private void updateArrayListAndSpinners(ArrayList<Map<SpannableString, Recipe>> recipeList) {
        // Update the ingredients TreeMap
        recipesList = recipeList;
        // Update the spinners with the new TreeMap data
        ArrayList<SpannableString> recipeNames = new ArrayList<>();
        for (Map<SpannableString, Recipe> recipeMap : recipesList) {
            for (SpannableString spannableString : recipeMap.keySet()) {
                recipeMap.put(InputFormatter.capitalize(spannableString), recipeMap.remove(spannableString));
            }
            recipeNames.addAll(recipeMap.keySet());
        }
        ArrayAdapter<SpannableString> ingredientsAdapter
                = new ArrayAdapter<>(RecipeActivity.this,
                R.layout.spinner_item_layout_recipe, recipeNames);
        ingredientsAdapter.insert(new SpannableString("Select Recipe"), 0);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipesSpinner.setAdapter(ingredientsAdapter);
    }
    private void highlightMatchingRecipes(ArrayList<Map<SpannableString, Recipe>> recipeList) {
        for (Map<SpannableString, Recipe> entry : recipesList) {
            Recipe currRecipe = entry.values().iterator().next();
            for (Map<SpannableString, Recipe> recipeMap : recipeList) {
                Recipe recipe = recipeMap.values().iterator().next();
                if (currRecipe.getRecipeID().equals(recipe.getRecipeID())) {
                    SpannableString recipeName = entry.keySet().iterator().next();
                    recipeName.setSpan(new BackgroundColorSpan(Color.GREEN), 0,
                            recipeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
    }

    private void getRecipeListAndHighlight() {
        viewModel.fetchRecipesbyIngredAvail(this::highlightMatchingRecipes);
    }

    private void sortBasedOnSwitch() {
        if (sortSwitch.isChecked()) {
            viewModel.fetchRecipesbyIngredAvail(sortedRecipeList -> {
                updateArrayListAndSpinners(sortedRecipeList);
                getRecipeListAndHighlight();
            });
        } else {
            viewModel.getRecipeList(recipeList -> {
                updateArrayListAndSpinners(recipeList);
                getRecipeListAndHighlight();
            });
        }
    }

    private void makeNavigationBar(ImageButton button, Intent intent) {
        button.setOnClickListener(v ->  startActivity(intent));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        RelativeLayout parentLayout = findViewById(R.id.activity_recipe);
        ImageButton toHomeButton = findViewById(R.id.btn_home);
        ImageButton toMealButton = findViewById(R.id.btn_meal);
        ImageButton toIngredientButton = findViewById(R.id.btn_ingredients);
        ImageButton toShoppingButton = findViewById(R.id.btn_shopping);
        ImageButton toPersonalButton = findViewById(R.id.btn_personal);
        ImageButton toFitnessButton = findViewById(R.id.btn_fitness);


        sortSwitch = findViewById(R.id.sortingSwitch);
        recipesSpinner = findViewById(R.id.recipesSpinner);
        Button popupButton = findViewById(R.id.btn_popup);

        viewModel = RecipeActivityViewModel.getInstance();
        viewModel.addObserver(this);
        viewModel.getRecipeList(recipes -> {
            updateArrayListAndSpinners(recipes);
            sortBasedOnSwitch();
        });

        Intent intentHome = new Intent(RecipeActivity.this, HomeActivity.class);
        makeNavigationBar(toHomeButton, intentHome);
        Intent intentMeal = new Intent(RecipeActivity.this, MealActivity.class);
        makeNavigationBar(toMealButton, intentMeal);
        Intent intentShopping = new Intent(RecipeActivity.this, ShoppingActivity.class);
        makeNavigationBar(toShoppingButton, intentShopping);
        Intent intentPersonal = new Intent(RecipeActivity.this, PersonalActivity.class);
        makeNavigationBar(toPersonalButton, intentPersonal);
        Intent intentIngredient = new Intent(RecipeActivity.this, IngredientsActivity.class);
        makeNavigationBar(toIngredientButton, intentIngredient);
        Intent intentFitness = new Intent(RecipeActivity.this, FitnessActivity.class);
        makeNavigationBar(toFitnessButton, intentFitness);

        sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> sortBasedOnSwitch());
        recipesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position <= 0) {
                    return;
                }
                Map<SpannableString, Recipe> selectedRecipeMap = recipesList.get(position - 1);
                viewModel.fetchRecipesbyIngredAvail(sortedRecipeList -> {
                    Recipe currRecipe = null;
                    for (Recipe recipe : selectedRecipeMap.values()) {
                        currRecipe = recipe;
                        break;
                    }
                    Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("recipeID", currRecipe.getRecipeID());
                    intent.putExtra("recipeName", InputFormatter.capitalize(currRecipe.getRecipeName()));
                    for (Map<SpannableString, Recipe> entry : sortedRecipeList) {
                        for (Recipe recipe : entry.values()) {
                            if (recipe.getRecipeID().equals(currRecipe.getRecipeID())) {
                                intent.putExtra("canMake", true);
                                startActivity(intent);
                                return;
                            }
                        }
                    }
                    startActivity(intent);
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        popupButton.setOnClickListener(v -> {
            showAddRecipePopup();
        });
        parentLayout.setOnTouchListener((v, event) -> {
            // Hide keyboard
            hideKeyboard();
            return false;
        });
    }
    private void showAddRecipePopup() {
        RecipeFormPopupActivity dialog = new RecipeFormPopupActivity();
        dialog.show(getSupportFragmentManager(), "RecipePopupDialogFragment");
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private HashMap<String, Integer> parseIngredientList(String ingredientList) {
        String[] ingredients = ingredientList.split(",");
        HashMap<String, Integer> ingredientsMap = new HashMap<>();
        for (String ingredient : ingredients) {
            String[] nameAndQuantity = ingredient.split(": ");
            if (nameAndQuantity.length == 2) {
                String ingredientName = nameAndQuantity[0].trim();
                if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)) {
                    Toast.makeText(RecipeActivity.this, "Invalid ingredient name "
                            + "detected!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                String ingredientQuantity = nameAndQuantity[1].trim();
                if (!InputValidator.isValidInputWithInteger(ingredientQuantity)) {
                    Toast.makeText(RecipeActivity.this, "Invalid quantity for "
                            + ingredientName + "!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                Integer ingredientQuantityInt = Integer.parseInt(nameAndQuantity[1].trim());
                ingredientsMap.put(ingredientName.toLowerCase(), ingredientQuantityInt);
            } else {
                Toast.makeText(RecipeActivity.this,
                        "Invalid input format for ingredient!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return ingredientsMap;
    }
    @Override
    public void updateRecipeScrollable() {
        sortBasedOnSwitch();
    }

}