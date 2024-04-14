package com.example.greenplate.views;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.greenplate.InputValidator;
import com.example.greenplate.R;
import com.example.greenplate.viewmodels.CreateAccountViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccountActivity extends AppCompatActivity {

    private CreateAccountViewModel viewModel;
    private EditText newUsernameEditText;
    private EditText newPasswordEditText;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        viewModel = CreateAccountViewModel.getInstance();
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        Button createAccount = findViewById(R.id.btn_create_account);
        Button toLoginButton = findViewById(R.id.btn_login);

        LinearLayout parentLayout = findViewById(R.id.activity_create_account);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        CreateAccountViewModel createAccountViewModeL = CreateAccountViewModel.getInstance();

        createAccount.setOnClickListener(v -> {
            String email = newUsernameEditText.getText().toString();
            String password = newPasswordEditText.getText().toString();
            if (!InputValidator.isValidInput(email)) {
                Toast.makeText(CreateAccountActivity.this, "Invalid email!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!InputValidator.isValidInput(password)) {
                Toast.makeText(CreateAccountActivity.this, "Invalid password!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            createAccountViewModeL.createAccount(email, password,
                    CreateAccountActivity.this);
        });
        toLoginButton.setOnClickListener(v ->  {
            Intent intent = new Intent(CreateAccountActivity.this,
                    LoginActivity.class);
            startActivity(intent);

        });
        createAccountViewModeL.getCreateSuccess().observe(this, loginSuccess -> {
            if (loginSuccess) {
                // Login successful, navigate to HomeActivity
                Toast.makeText(CreateAccountActivity.this, "Account creation successful.",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateAccountActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish(); // Close LoginActivity
            }
        });

        // Set touch listener to hide keyboard when touched outside EditText
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