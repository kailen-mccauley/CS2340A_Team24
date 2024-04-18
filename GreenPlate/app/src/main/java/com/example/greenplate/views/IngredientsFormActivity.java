package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.utilities.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;

public class IngredientsFormActivity extends AppCompatActivity {

    private EditText ingredientNameEditText;
    private EditText caloriesEditText;
    private EditText quantityEditText;
    private IngredientsActivityViewModel ingredientsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_form);
        Button toIngredientsScreen = findViewById(R.id.btn_to_ingredient_screen);
        Button btnSubmitIngredient = findViewById(R.id.btn_submit_ingredient);
        RelativeLayout parentLayout = findViewById(R.id.activity_input_ingredients);
        ingredientNameEditText  = findViewById(R.id.ingredientNameEditText);
        caloriesEditText = findViewById(R.id.caloriesEditText);
        quantityEditText = findViewById(R.id.quantityEditText);

        ingredientsViewModel = IngredientsActivityViewModel.getInstance();


        toIngredientsScreen.setOnClickListener(v ->  {
            Intent intent = new Intent(IngredientsFormActivity.this,
                    IngredientsActivity.class);
            startActivity(intent);
        });

        btnSubmitIngredient.setOnClickListener(v ->  {
            String ingredientName = ingredientNameEditText.getText().toString();
            String calories = caloriesEditText.getText().toString();
            String quantity = quantityEditText.getText().toString();
            if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)) {
                Toast.makeText(IngredientsFormActivity.this,
                        "Please enter a valid ingredient name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(calories)) {
                Toast.makeText(IngredientsFormActivity.this,
                        "Please enter a valid integer value for calories!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(quantity)) {
                Toast.makeText(IngredientsFormActivity.this,
                        "Please enter a valid integer value for quantity!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // IMPLEMENT LOGIC TO STORE INGREDIENT --------
            int inputCals = Integer.parseInt(calories);
            int inputQuantity = Integer.parseInt(quantity);

            ingredientsViewModel.storeIngredient(ingredientName, inputCals, inputQuantity,
                    IngredientsFormActivity.this);
            ingredientNameEditText.setText("");
            caloriesEditText.setText("");
            quantityEditText.setText("");
            hideKeyboard();
        });

        parentLayout.setOnTouchListener((v, event) -> {
            // Hide keyboard
            hideKeyboard();
            return false;
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

