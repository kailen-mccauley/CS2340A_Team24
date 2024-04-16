package com.example.greenplate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.greenplate.ShoppingValidator;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;

public class ShoppingValidatorUnitTest {
    @Test
    public void testInvalidUser() {
        assertFalse(ShoppingValidator.isValidUser(null));
    }

    @Test
    public void testNegativeQuantity() {
        assertFalse(ShoppingValidator.isValidQuantity(-5));
    }

    @Test
    public void testZeroQuantity() {
        assertFalse(ShoppingValidator.isValidQuantity(0));
    }

    @Test
    public void testPositiveQuantity() {
        assertTrue(ShoppingValidator.isValidQuantity(2));
    }

    @Test
    public void testNullIngredient() {
        assertFalse(ShoppingValidator.isValidIngredient(null));
    }

    @Test
    public void testEmptyIngredient() {
        assertFalse(ShoppingValidator.isValidIngredient(""));
    }

    @Test
    public void testValidIngredient() {
        assertTrue(ShoppingValidator.isValidIngredient("valid ingredient"));
    }

    @Test
    public void testSpacesIngredient() {
        assertFalse(ShoppingValidator.isValidIngredient(" space "));
    }

    @Test
    public void testNumericIngredient() {
        assertFalse(ShoppingValidator.isValidIngredient("9;8"));
    }

    @Test
    public void testDuplicateIngredients() {
        List<String> items = new ArrayList<>();
        items.add("waffles");
        items.add("waffles");
        assertFalse(ShoppingValidator.isValidBuyItemsList(items));
    }

    @Test
    public void testUniqueIngredients() {
        List<String> items = new ArrayList<>();
        items.add("waffles");
        items.add("onions");
        assertTrue(ShoppingValidator.isValidBuyItemsList(items));
    }

    @Test
    public void testEmptyIngredients() {
        List<String> items = new ArrayList<>();
        assertFalse(ShoppingValidator.isValidBuyItemsList(items));
    }
}
