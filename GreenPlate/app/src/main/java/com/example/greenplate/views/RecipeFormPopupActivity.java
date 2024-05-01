package com.example.greenplate.views;
import com.example.greenplate.R;
import com.example.greenplate.utilites.InputValidator;
import com.example.greenplate.utilites.ValueExtractor;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.*;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;


public class RecipeFormPopupActivity extends DialogFragment {
    private RecipeActivityViewModel viewModel;
    private EditText recipeNameEditText;
    private EditText ingredientListEditText;

    private void resizePopup(View view, RelativeLayout parentLayout) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Calculate the desired width for the dialog (e.g., 80% of the screen width)
        int dialogWidth = (int) (screenWidth * 0.8);

        ViewGroup.LayoutParams layoutParams = parentLayout.getLayoutParams();
        layoutParams.width = dialogWidth;
        parentLayout.setLayoutParams(layoutParams);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recipe_form_popup, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout parentLayout = view.findViewById(R.id.activity_recipe_form_popup);
        resizePopup(view, parentLayout);

        Button submitRecipe = view.findViewById(R.id.btn_submit_recipe);
        Button closeButton = view.findViewById(R.id.btn_close_recipe_form);
        recipeNameEditText = view.findViewById(R.id.recipeNameEditText);
        ingredientListEditText = view.findViewById(R.id.recipeIngredientsEditText);
        viewModel = RecipeActivityViewModel.getInstance();
        submitRecipe.setOnClickListener(v -> {
            String recipeName = ValueExtractor.extract(recipeNameEditText).toLowerCase();
            String ingredientList = ValueExtractor.extract(ingredientListEditText);
            if (!InputValidator.isValidInputWithSpacesBetween(recipeName)) {
                Toast.makeText(requireContext(),
                        "Please input a name for your recipe!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithSpacesBetween(ingredientList)) {
                Toast.makeText(requireContext(), "Please input the ingredient list "
                        + "for your recipe!", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, Integer> ingredientsMap = parseIngredientList(ingredientList);
            if (ingredientsMap != null) {
                recipeNameEditText.setText("");
                ingredientListEditText.setText("");
                Toast.makeText(requireContext(), "Recipe  " + recipeName
                        + "  submitted successfully!", Toast.LENGTH_SHORT).show();
                viewModel.storeRecipe(recipeName, ingredientsMap);
                hideKeyboard(view);
            }
        });
        closeButton.setOnClickListener(v -> dismiss());
        parentLayout.setOnTouchListener((v, event) -> {
            hideKeyboard(view);
            return false;
        });
    }
    private HashMap<String, Integer> parseIngredientList(String ingredientList) {
        String[] ingredients = ingredientList.split(",");
        HashMap<String, Integer> ingredientsMap = new HashMap<>();
        for (String ingredient : ingredients) {
            String[] nameAndQuantity = ingredient.split(": ");
            if (nameAndQuantity.length == 2) {
                String ingredientName = nameAndQuantity[0].trim();
                if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)) {
                    Toast.makeText(requireContext(), "Invalid ingredient name "
                            + "detected!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                String ingredientQuantity = nameAndQuantity[1].trim();
                if (!InputValidator.isValidInputWithInteger(ingredientQuantity)) {
                    Toast.makeText(requireContext(), "Invalid quantity for "
                            + ingredientName + "!", Toast.LENGTH_SHORT).show();
                    return null;
                }
                Integer ingredientQuantityInt = Integer.parseInt(nameAndQuantity[1].trim());
                ingredientsMap.put(ingredientName.toLowerCase(), ingredientQuantityInt);
            } else {
                Toast.makeText(requireContext(),
                        "Invalid input format for ingredient!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return ingredientsMap;
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
