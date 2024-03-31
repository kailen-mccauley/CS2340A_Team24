package com.example.greenplate;

import android.text.SpannableString;

import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.ArrayList;
import java.util.Map;

public interface SortRecipe {
    ArrayList<Map<SpannableString, String>> sortRecipes(RecipeActivityViewModel viewmodel);
}