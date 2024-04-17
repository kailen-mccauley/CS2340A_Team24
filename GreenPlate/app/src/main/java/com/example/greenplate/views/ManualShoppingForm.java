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

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

public class ManualShoppingForm extends AppCompatActivity {

    private EditText ingredientTitle;
    private EditText calSection;
    private EditText quantityNum;
    private ShoppingListActivityViewModel shoppingListActivityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_form);
        Button toShoppingScreen = findViewById(R.id.btn_return_shopping_screen);

        Button btnSubmit = findViewById(R.id.btn_submit_manual);

        RelativeLayout parentLayout = findViewById(R.id.activity_manual_shopping);

        ingredientTitle = findViewById(R.id.nameSpace);
        calSection = findViewById(R.id.calSection);
        quantityNum = findViewById(R.id.quantityNumber);

        shoppingListActivityViewModel = ShoppingListActivityViewModel.getInstance();

        toShoppingScreen.setOnClickListener(v ->  {
            Intent intent = new Intent(ManualShoppingForm.this,
                    ShoppingActivity.class);
            startActivity(intent);
        });

        btnSubmit.setOnClickListener(v ->  {
            String ingredientName = ingredientTitle.getText().toString();
            String quantity = quantityNum.getText().toString();
            String calories = calSection.getText().toString();

            if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)) {
                Toast.makeText(ManualShoppingForm.this,
                        "Please enter a valid ingredient name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(calories)) {
                Toast.makeText(ManualShoppingForm.this,
                        "Please enter a valid integer value for calories!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(quantity)) {
                Toast.makeText(ManualShoppingForm.this,
                        "Please enter a valid integer value for quantity!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int recordCalories = Integer.parseInt(calories);
            int recordQuantity = Integer.parseInt(quantity);

            shoppingListActivityViewModel.storeShoppingListItem(ingredientName, recordQuantity,
                    () -> { });
            ingredientTitle.setText("");
            calSection.setText("");
            quantityNum.setText("");
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

