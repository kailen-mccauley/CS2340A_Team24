package com.example.greenplate;
import static org.junit.Assert.assertEquals;
import com.example.greenplate.models.Ingredient;

import org.junit.Before;
import org.junit.Test;

public class IngredientModelUnitTest {
    Ingredient ingredient;
    @Before
    public void start() {
        ingredient = new Ingredient("flour", 50, 5, "userId");
    }

    @Test
    public void testGetIngredientName() {
        assertEquals("flour", ingredient.getIngredientName());
    }

    @Test
    public void testGetCalories() {
        assertEquals(50, ingredient.getCalories());
    }

    @Test
    public void testGetUserId() {
        assertEquals("userId", ingredient.getUserId());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(5, ingredient.getQuantity());
    }
}