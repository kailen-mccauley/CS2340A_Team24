package com.example.greenplate;
import com.example.greenplate.models.Ingredient;
import com.example.greenplate.viewmodels.IngredientsActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class IngredientActivityUnitTest {
    private IngredientsActivityViewModel viewModel;

    @Before
    public void start() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        viewModel = IngredientsActivityViewModel.getInstance();
    }

    @Test
    public void testDuplicateIngredients() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                viewModel.getSortedIngredients(sortedIngredients -> {
                    viewModel.storeIngredient("eggs", 100, 1, null);
                    viewModel.storeIngredient("eggs", 100, 1, null);
                    viewModel.getSortedIngredients(updatedIngredients -> {
                        int count = 0;
                        for (Ingredient ing : updatedIngredients) {
                            if (ing.getIngredientName().equalsIgnoreCase("eggs")) {
                                count++;
                            }
                        }
                        assertFalse(count > 1);
                    });
                });
            }
        });
    }

    @Test
    public void testNonPositiveQuanitity() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                viewModel.storeIngredient("milk", 50, -3, null);
            }
            viewModel.getSortedIngredients(updatedIngredients -> {
                boolean negativeQuantity = false;
                for (Ingredient ing : updatedIngredients) {
                    if (ing.getQuantity() == -3) {
                        negativeQuantity = true;
                    }
                }
                assertFalse(negativeQuantity);
            });
        });
    }

    @Test
    public void testAddNewIngredientToPantry() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                viewModel.storeIngredient("milk", 50, 3, null);
            }
            viewModel.getSortedIngredients(updatedIngredients -> {
                boolean present = false;
                for (Ingredient ing : updatedIngredients) {
                    if (ing.getQuantity() == 3 && ing.getIngredientName().equalsIgnoreCase("milk") && ing.getCalories() == 50) {
                        present = true;
                    }
                }
                assertTrue(present);
            });
        });
    }

    @Test
    public void testRemoveIngredientToPantry() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                viewModel.storeIngredient("milk", 50, 3, null);
                viewModel.decreaseIngredientQuantityAndTreeMap("milk", 3, null, null);;
            }
            viewModel.getSortedIngredients(updatedIngredients -> {
                boolean present = true;
                for (Ingredient ing : updatedIngredients) {
                    if (ing.getIngredientName().equalsIgnoreCase("milk")) {
                        present = false;
                        break;
                    }
                }
                assertFalse(present);
            });
        });
    }
}