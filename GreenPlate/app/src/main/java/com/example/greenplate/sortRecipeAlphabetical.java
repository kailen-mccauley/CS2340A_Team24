package com.example.greenplate;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.example.greenplate.models.Ingredient;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class sortRecipeAlphabetical implements sortRecipe {
    @Override
    public ArrayList<Map<SpannableString, String>> sortRecipes(RecipeActivityViewModel viewModel) {
        ArrayList<Map<SpannableString, String>> sortedRecipes = new ArrayList<>();
        viewModel.getRecipelist(new RecipeActivityViewModel.RecipeListListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> recipeList) {

                // The list we are getting from the viewModel/firebase is already sorted alphabetically
                for (Map<SpannableString, Recipe> recipe : recipeList) {

                    // for that map entry, get the name (there is only 1 mapping in the map but we need this for loop)
                    for (SpannableString recipeName : recipe.keySet()) {

                        // Create a Map so we can map the recipe name to the recipe id
                        Map<SpannableString, String> temp = new HashMap<>();

                        viewModel.doesUserHaveIngredients(recipe.get(recipeName), new RecipeActivityViewModel.IngredientCheckListener() {
                            @Override
                            public void onIngredientCheckResult(boolean hasIngredients) {
                                if (hasIngredients) {
                                    // Highlight recipe name in green
                                    recipeName.setSpan(new ForegroundColorSpan(Color.GREEN), 0, recipeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                            }
                        });

                        // Creates single entry in the temp map
                        temp.put(recipeName, recipe.get(recipeName).getRecipeID());
                        // Adds to our sorted array list
                        sortedRecipes.add(temp);
                    }
                }


            }
        });
        return sortedRecipes;
    }
}
