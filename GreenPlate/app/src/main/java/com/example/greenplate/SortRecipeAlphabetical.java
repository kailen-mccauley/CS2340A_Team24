package com.example.greenplate;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortRecipeAlphabetical implements SortRecipe {
    @Override
    public ArrayList<Map<SpannableString, String>> sortRecipes(RecipeActivityViewModel viewModel) {
        ArrayList<Map<SpannableString, String>> sortedRecipes = new ArrayList<>();
        viewModel.getRecipeList(new RecipeActivityViewModel.RecipeListListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Map<SpannableString, Recipe>> recipeList) {
                for (Map<SpannableString, Recipe> recipe : recipeList) {
                    for (SpannableString recipeName : recipe.keySet()) {
                        Map<SpannableString, String> temp = new HashMap<>();
                        viewModel.doesUserHaveIngredients(recipe.get(recipeName),
                                new RecipeActivityViewModel.IngredientCheckListener() {
                                @Override
                                public void onIngredientCheckResult(boolean hasIngredients) {
                                    if (hasIngredients) {
                                        recipeName.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                                                recipeName.length(), Spannable
                                                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                }
                            });
                        temp.put(recipeName, recipe.get(recipeName).getRecipeID());
                        sortedRecipes.add(temp);
                    }
                }


            }
        });
        return sortedRecipes;
    }
}