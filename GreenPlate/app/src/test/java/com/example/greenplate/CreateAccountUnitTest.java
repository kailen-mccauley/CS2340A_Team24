package com.example.greenplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals(false, CreateAccountViewModel.getInstance().isValidUsernameOrPassword(null, null));
    }
    @Test
    public void testEmptyInputs() {
        assertEquals(false, CreateAccountViewModel.getInstance().isValidUsernameOrPassword("",""));
    }

    @Test
    public void testWhitespaceInputs() {
        assertEquals(false, CreateAccountViewModel.getInstance().isValidUsernameOrPassword("   ","   "));
    }
}