package com.example.greenplate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.greenplate.viewmodels.MealActivityViewModel;

import org.junit.Test;

public class MealActivityUnitTest {
    @Test
    public void testMealEntryStored() {
        MealActivityViewModel.getInstance().storeMeal("meal", "500");
    }
}
