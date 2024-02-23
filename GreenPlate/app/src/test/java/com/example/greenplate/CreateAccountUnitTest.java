package com.example.greenplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CreateAccountUnitTest {
    @Test
    public void testNullInputs() {
        assertTrue(CreateAccountViewModel.getInstance().isValidUsernameOrPassword(null, null));
    }
}

