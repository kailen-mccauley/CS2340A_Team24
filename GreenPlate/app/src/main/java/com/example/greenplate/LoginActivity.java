package com.example.greenplate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText usernameEditText;
    private EditText passwordEditText;

    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button toHomeButton = findViewById(R.id.btn_login);
        Button toCreateAccountButton = findViewById(R.id.btn_sign_up);
        Button toCloseApplication = findViewById(R.id.btn_close_app);


        // TODO:
        //  when a user clicks login (toHomeButton) there account information should be
        //  verified. If it does not match an account in firebase, they should be told that
        //  there login info is wrong
        toHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        toCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUsernameAndPassword(usernameEditText, passwordEditText)) {
                    Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                    startActivity(intent);
                } else {
                    // not sure what to do here
                }

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
