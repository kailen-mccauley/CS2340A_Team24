package com.example.greenplate.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.InputValidator;
import com.example.greenplate.viewmodels.LoginViewModel;
import com.example.greenplate.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = LoginViewModel.getInstance();
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button toHomeButton = findViewById(R.id.btn_login);
        Button toCreateAccountButton = findViewById(R.id.btn_sign_up);
        Button toCloseApplication = findViewById(R.id.btn_close_app);
        LinearLayout parentLayout = findViewById(R.id.activity_login);

        toHomeButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!InputValidator.isValidInput(email)) {
                Toast.makeText(LoginActivity.this, "Invalid email!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInput(password)) {
                Toast.makeText(LoginActivity.this, "Invalid password!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.login(email, password, LoginActivity.this);

        });

        toCreateAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });

        toCloseApplication.setOnClickListener(v -> finishAffinity());

        viewModel.getLoginSuccess().observe(this, loginSuccess -> {
            if (loginSuccess) {
                // Login successful, navigate to HomeActivity
                Toast.makeText(LoginActivity.this, "Login successful.",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Close LoginActivity
            }
        });
        parentLayout.setOnTouchListener((v, event) -> {
            // Hide keyboard
            hideKeyboard();
            return false;
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
