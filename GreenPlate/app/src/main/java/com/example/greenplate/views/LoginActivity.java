package com.example.greenplate.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        mAuth = FirebaseAuth.getInstance();


        // TODO:
        //  when a user clicks login (toHomeButton) there account information should be
        //  verified. If it does not match an account in firebase, they should be told that
        //  there login info is wrong
        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (checkUsernameAndPassword(usernameEditText, passwordEditText)) {
                    if (LoginViewModel.getInstance().login(email,password, mAuth, LoginActivity.this)) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // Show a message indicating that email or password is empty
                    Toast.makeText(LoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        toCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        toCloseApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
    public boolean checkUsernameAndPassword(EditText usernameEditText, EditText passwordEditText) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        return viewModel.isValidUsernameOrPassword(username, password);
    }
}