package com.example.greenplate.viewmodels;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.greenplate.views.LoginActivity;
import com.google.android.gms.tasks.Task;

public class LoginViewModel {
    private static volatile LoginViewModel instance;
    private FirebaseAuth mAuth;

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

    public void login(String email, String password, LoginActivity loginActivity,
                      LoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginActivity, task ->  {
                    if (task.isSuccessful()) {
                        listener.onLoginComplete();
                    } else {
                        listener.onLoginFailed(task);
                    }
                });
    }
    public interface LoginListener {
        void onLoginComplete();
        void onLoginFailed(Task<AuthResult> task);
    }
}
