package com.example.greenplate.views;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.models.Recipe;
import com.example.greenplate.viewmodels.RecipeActivityViewModel;

import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private RecipeActivityViewModel RecipeViewModel;
    private TextView ingredientNameText;
    private TextView quantityIngredientText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);
        Button toRecipeScreen = findViewById(R.id.btn_to_recipe_screen);
        RelativeLayout parentLayout = findViewById(R.id.activity_input_ingredients);

        RecipeViewModel = RecipeActivityViewModel.getInstance();

        Intent intent = getIntent();
        Log.d("DetailActivity", "Recipe object: " + intent.getStringExtra("recipeID"));
        if (intent != null && intent.hasExtra("recipeID")) {
            String recipeID = intent.getStringExtra("recipeID");
            if (recipeID != null) {
                RecipeViewModel.getRecipeDetails(recipeID, new RecipeActivityViewModel.RecipeDetailsListener() {
                    @Override
                    public void onRecipeDetailsReceived(Recipe recipe) {
                        LinearLayout scrollable = findViewById(R.id.scrollableLay);
                        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                            String ingredientName = entry.getKey();
                            int quantity = entry.getValue();

                            LinearLayout ingredientLayout = new LinearLayout(DetailActivity.this);
                            ingredientLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);

                            TextView ingredientNameTextView = new TextView(DetailActivity.this);
                            LinearLayout.LayoutParams ingredientNameParams = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            ingredientNameParams.width = 0;
                            ingredientNameTextView.setLayoutParams(ingredientNameParams);
                            ingredientNameTextView.setText(ingredientName);
                            ingredientNameTextView.setTextSize(22);
                            ingredientNameTextView.setTextColor(getResources().getColor(R.color.pennBlue));

                            TextView quantityTextView = new TextView(DetailActivity.this);
                            LinearLayout.LayoutParams quantityParams = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    1
                            );
                            quantityParams.width = 0;
                            quantityParams.setMarginStart(60);
                            quantityTextView.setLayoutParams(quantityParams);
                            quantityTextView.setText(String.valueOf(quantity));
                            quantityTextView.setTextSize(22);
                            quantityTextView.setTextColor(getResources().getColor(R.color.pennBlue));

                            ingredientLayout.addView(ingredientNameTextView);
                            ingredientLayout.addView(quantityTextView);

                            scrollable.addView(ingredientLayout);
                        }
                    }

                    @Override
                    public void onRecipeDetailsError(String errorMessage) {
                    }
                });
            }
        } else {
            Log.d("Detail Activity", "Recipe ID not found in intent extras");
        }

        toRecipeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,
                        RecipeActivity.class);
                startActivity(intent);
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

