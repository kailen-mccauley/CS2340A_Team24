package com.example.greenplate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.greenplate.utilites.InputValidator;

import org.junit.Test;

public class InputValidatorUnitTest {
    @Test
    public void testNullInput() {
        assertFalse(InputValidator.isValidInput(null));
    }
    @Test
    public void testEmptyInput() {
        assertFalse(InputValidator.isValidInput(""));
    }

    @Test
    public void testWhitespaceInput() {
        assertFalse(InputValidator.isValidInput(" "));
    }

    @Test
    public void testInvalidUsernameAndPassword() {
        assertFalse(InputValidator.isValidInput("newUser 10@gmail.com "));
        assertFalse(InputValidator.isValidInput("abc 123 "));
    }

    @Test
    public void testValidUsernameAndPassword() {
        assertTrue(InputValidator.isValidInput("kailen.mccauley@gmail.com"));
        assertTrue(InputValidator.isValidInput("abc123"));
    }

    @Test
    public void testInvalidMealNameLeadingSpace() {
        assertFalse(InputValidator.isValidInputWithSpacesBetween(" sugar cookies"));
    }

    @Test
    public void testInvalidMealNameTrailingSpace() {
        assertFalse(InputValidator.isValidInputWithSpacesBetween("baked potato "));
    }

    @Test
    public void testValidMealName() {
        assertTrue(InputValidator.isValidInputWithSpacesBetween("Chocolate Shake"));
    }

    @Test
    public void testValidMealNameMultiSpace() {
        assertTrue(InputValidator.isValidInputWithSpacesBetween("Sweet and Sour Chicken"));
    }

    @Test
    public void testInvalidCalorieNoInt() {
        assertFalse(InputValidator.isValidInputWithInteger("Calories"));
    }

    @Test
    public void testInvalidCalorie() {
        assertFalse(InputValidator.isValidInputWithInteger("10Calories"));
    }

    @Test
    public void testValidCalorie() {
        assertTrue(InputValidator.isValidInputWithInteger("10"));
    }

    @Test
    public void testInvalidHeight() {
        assertFalse(InputValidator.isValidInputWithInteger("170 cm"));
    }

    @Test
    public void testInvalidHeight2() {
        assertFalse(InputValidator.isValidInputWithInteger("199cm"));
    }

    @Test
    public void testValidHeight() {
        assertTrue(InputValidator.isValidInputWithInteger("200"));
    }

    @Test
    public void testInvalidWeight() {
        assertFalse(InputValidator.isValidInputWithInteger("180 kg"));
    }

    @Test
    public void testInvalidWeight2() {
        assertFalse(InputValidator.isValidInputWithInteger("110kg"));
    }

    @Test
    public void testValidWeight() {
        assertTrue(InputValidator.isValidInputWithInteger("50"));
    }
}
