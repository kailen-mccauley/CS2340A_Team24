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

public class LoginViewModel {
    private static LoginViewModel instance;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);

    public static synchronized LoginViewModel getInstance() {
        if (instance == null) {
            instance = new LoginViewModel();
        }
        return instance;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean success) {
        loginSuccess.setValue(success);
    }

    public boolean isValidUsernameOrPassword(String username, String password) {
        return username != null && !username.contains(" ") && !username.isEmpty()
                && password != null && !password.contains(" ") && !password.isEmpty();
    }

    public void login(String email, String password, FirebaseAuth mAuth, LoginActivity LoginActivity) {
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(LoginActivity, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}
