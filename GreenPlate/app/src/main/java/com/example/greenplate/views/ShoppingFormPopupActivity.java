package com.example.greenplate.views;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.greenplate.utilites.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.utilites.ValueExtractor;
import com.example.greenplate.viewmodels.ShoppingListActivityViewModel;

public class ShoppingFormPopupActivity extends DialogFragment {

    private EditText ingredientTitle;
    private EditText calSection;
    private EditText quantityNum;
    private ShoppingListActivityViewModel shoppingListActivityViewModel;
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
        return inflater.inflate(R.layout.activity_shopping_form, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        RelativeLayout parentLayout = view.findViewById(R.id.activity_manual_shopping);
        resizePopup(view, parentLayout);

        Button toShoppingScreen = view.findViewById(R.id.btn_return_shopping_screen);
        Button btnSubmit = view.findViewById(R.id.btn_submit_manual);

        ingredientTitle = view.findViewById(R.id.nameSpace);
        calSection = view.findViewById(R.id.calSection);
        quantityNum = view.findViewById(R.id.quantityNumber);

        shoppingListActivityViewModel = ShoppingListActivityViewModel.getInstance();

        btnSubmit.setOnClickListener(v ->  {
            String ingredientName = ValueExtractor.extract(ingredientTitle).toLowerCase();
            String quantity = ValueExtractor.extract(quantityNum);
            String calories = ValueExtractor.extract(calSection);

            if (!InputValidator.isValidInputWithSpacesBetween(ingredientName)) {
                Toast.makeText(requireContext(),
                        "Please enter a valid ingredient name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(calories)) {
                Toast.makeText(requireContext(),
                        "Please enter a valid integer value for calories!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInputWithInteger(quantity)) {
                Toast.makeText(requireContext(),
                        "Please enter a valid integer value for quantity!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int recordCalories = Integer.parseInt(calories);
            int recordQuantity = Integer.parseInt(quantity);

            shoppingListActivityViewModel.storeShoppingListItem(ingredientName,
                    recordQuantity, recordCalories,
                    () -> { });
            ingredientTitle.setText("");
            calSection.setText("");
            quantityNum.setText("");
            hideKeyboard(view);
        });

        toShoppingScreen.setOnClickListener(v -> dismiss());

        parentLayout.setOnTouchListener((v, event) -> {
            // Hide keyboard
            hideKeyboard(view);
            return false;
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

