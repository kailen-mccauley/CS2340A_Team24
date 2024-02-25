package com.example.greenplate.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.greenplate.views.CreateAccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.firebase.*;
import com.google.android.gms.tasks.OnCompleteListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.*;
import com.google.firebase.*;
import com.google.android.gms.tasks.OnCompleteListener;



public class CreateAccountViewModel {
    private static CreateAccountViewModel instance;

    private MutableLiveData<Boolean> createSuccess = new MutableLiveData<>(false);
    public static synchronized CreateAccountViewModel getInstance() {
        if (instance == null) {
            instance = new CreateAccountViewModel();
        }
        return instance;
    }

    public LiveData<Boolean> getCreeateSuccess() {
        return createSuccess;
    }

    public void setCreateSuccess(boolean success) {
        createSuccess.setValue(success);
    }

    public boolean isValidUsernameOrPassword(String username, String password) {
        return username != null && !username.contains(" ") && !username.isEmpty()
                && password != null && !password.contains(" ") && !password.isEmpty();
    }

    public void createAccount(String email, String password, FirebaseAuth mAuth, CreateAccountActivity CreateAccountActivity) {
        // if userName and password pass check, we must create an account before going to login
        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setCreateSuccess(true);
                            //updateUI(user);
                            // Proceed to login screen
//                            Intent intent = new Intent(CreateAccountActivity, LoginActivity.class);
//                            startActivity(intent);
                            Toast.makeText(CreateAccountActivity, "lOggin WOrked asklnfkf", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign up fails, display a message to the user.
                            setCreateSuccess(false);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity, "lOggin WOrked asklnfkf", Toast.LENGTH_SHORT).show();
                            Toast.makeText(CreateAccountActivity, "Authentication failed."+ task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
