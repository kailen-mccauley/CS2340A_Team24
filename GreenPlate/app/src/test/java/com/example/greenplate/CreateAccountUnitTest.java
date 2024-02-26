package com.example.greenplate;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.greenplate.viewmodels.CreateAccountViewModel;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CreateAccountUnitTest {
    @Test
    public void testNullInputs() {
        assertFalse(CreateAccountViewModel.getInstance().isValidUsernameOrPassword(null, null));
    }
    @Test
    public void testEmptyInputs() {
        assertFalse(CreateAccountViewModel.getInstance().isValidUsernameOrPassword("",""));
    }

    @Test
    public void testWhitespaceInputs() {
        assertFalse(CreateAccountViewModel.getInstance().isValidUsernameOrPassword("   ","   "));
    }

    @Test
    public void testValidInputs() {
        assertTrue(CreateAccountViewModel.getInstance().isValidUsernameOrPassword("newUser","newPassword"));
    }
}