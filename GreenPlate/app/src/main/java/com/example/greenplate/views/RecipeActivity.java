package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.sortRecipeAlphabetical;
import com.example.greenplate.sortRecipeUserHasIngredients;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    private RecipeActivityViewModel viewModel;
    private EditText recipeNameEditText;
    private EditText ingredientListEditText;

    private ArrayList<Map<SpannableString, String>> recipesArrayList;

    private void updateArrayListAndSpinners(ArrayList<Map<SpannableString, String>> recipeList, Spinner recipesSpinner) {
        // Update the ingredients TreeMap
        recipesArrayList = recipeList;
        // Update the spinners with the new TreeMap data
        ArrayList<SpannableString> recipeNames = new ArrayList<>();
        for (Map<SpannableString, String> recipeMap : recipesArrayList) {
            recipeNames.addAll(recipeMap.keySet());
        }
        ArrayAdapter<SpannableString> ingredientsAdapter = new ArrayAdapter<>(RecipeActivity.this, R.layout.spinner_item_layout_recipe, recipeNames);
        ingredientsAdapter.insert(new SpannableString("Select ingredient"), 0);
        ingredientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipesSpinner.setAdapter(ingredientsAdapter);
    }

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
        viewModel = RecipeActivityViewModel.getInstance();


        viewModel.getRecipelist(new RecipeActivityViewModel.RecipeListListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> recipeList) {
                updateArrayListAndSpinners(viewModel.getSorted(), recipesSpinner);
            }
        });


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
                    Log.d("test1", "test1");
                    viewModel.fetchAndSortRecipesByIngredientsAvailability(new RecipeActivityViewModel.RecipeListListener() {
                        @Override
                        public void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> sortedRecipeList) {
                            Log.d("test2", "test2");
                            updateArrayListAndSpinners(sortedRecipeList, recipesSpinner);
                        }
                    });
                } else {
                    // SORTING ALPHABETICALLY
                    viewModel.setSortRecipeInstance(new sortRecipeAlphabetical());
                    viewModel.getRecipelist(new RecipeActivityViewModel.RecipeListListener() {
                        @Override
                        public void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> recipeList) {
                            updateArrayListAndSpinners(recipeList, recipesSpinner);
                        }
                    });
                }
            }
        });

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipeName = recipeNameEditText.getText().toString().toLowerCase();
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
                HashMap<String, Integer> ingredientsMap = new HashMap<>();
                for (String ingredient : ingredients) {
                    String[] nameAndQuantity = ingredient.split(": ");
                    if (nameAndQuantity.length == 2) {
                        String ingredientName = nameAndQuantity[0].trim();
                        if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)){
                            Toast.makeText(RecipeActivity.this,
                                    "Invalid ingredient name detected!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String ingredientQuantity = nameAndQuantity[1].trim();
                        if (!InputValidator.isValidInputWithInteger(ingredientQuantity)){
                            Toast.makeText(RecipeActivity.this,
                                    "Invalid quantity for " + ingredientName + "!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Integer ingredientQuantityInt = Integer.parseInt(nameAndQuantity[1].trim());
                        ingredientsMap.put(ingredientName.toLowerCase(), ingredientQuantityInt);
                    } else {
                        Toast.makeText(RecipeActivity.this,
                                "Invalid input format for ingredient!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                //TODO
                // Send the ingredientsMap over to the viewmodel
                recipeNameEditText.setText("");
                ingredientListEditText.setText("");
                Toast.makeText(RecipeActivity.this,
                        "Recipe  " + recipeName + "  submitted successfully!",
                        Toast.LENGTH_SHORT).show();
                viewModel.storeRecipe(recipeName, ingredientsMap);
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