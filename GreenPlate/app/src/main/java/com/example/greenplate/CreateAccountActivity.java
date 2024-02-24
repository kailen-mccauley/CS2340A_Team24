package com.example.greenplate;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.firebase.*;
import com.google.android.gms.tasks.OnCompleteListener;



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
        Button toLoginButton = findViewById(R.id.btn_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();




        // TODO:
        //  when create account is clicked, the account needs to be added to firebase before
        //  the screen swithces from account back to login
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = newUsernameEditText.getText().toString();
                String password = newPasswordEditText.getText().toString();
                if(checkUsernameAndPassword(newUsernameEditText, newPasswordEditText)) {
                    if (CreateAccountViewModel.getInstance().createAccount(email, password, mAuth, CreateAccountActivity.this)) {
                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    // Handle invalid username or password
                    // For example, show an error message
                    Toast.makeText(CreateAccountActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }
    public boolean checkUsernameAndPassword(EditText newUsernameEditText, EditText newPasswordEditText) {
        String username = newUsernameEditText.getText().toString();
        String password = newPasswordEditText.getText().toString();
        return viewModel.isValidUsernameOrPassword(username, password);
    }
}