package com.example.greenplate.viewmodels;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.greenplate.views.LoginActivity;

public class LoginViewModel {
    private static volatile LoginViewModel instance;
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);

    private LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }
    public static LoginViewModel getInstance() {
        if (instance == null) {
            synchronized (PersonalActivityViewModel.class) {
                if (instance == null) {
                    instance = new LoginViewModel();
                }
            }
        }
        return instance;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean success) {
        loginSuccess.setValue(success);
    }

    public void login(String email, String password, LoginActivity loginActivity) {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setLoginSuccess(true);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            setLoginSuccess(false);

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginActivity,
                                    "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
