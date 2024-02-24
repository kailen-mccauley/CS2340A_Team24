package com.example.greenplate;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.greenplate.viewmodels.LoginViewModel;

public class LoginViewModelTest {

    @Test
    public void testValidUsernameAndPassword() {
        assertTrue(LoginViewModel.getInstance().isValidUsernameOrPassword("user123", "password123"));
    }

    @Test
    public void testNullInputs() {
        assertFalse(LoginViewModel.getInstance().isValidUsernameOrPassword(null, null));
    }
}
