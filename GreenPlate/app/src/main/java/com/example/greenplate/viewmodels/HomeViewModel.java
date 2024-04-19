package com.example.greenplate.viewmodels;

import android.content.SharedPreferences;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;


public class HomeViewModel {

    private static volatile HomeViewModel instance;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    private HomeViewModel(Context context) {
        sharedPreferences = context.getSharedPreferences("FitnessPrefs", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public static HomeViewModel getInstance(Context context) {
        if (instance == null) {
            synchronized (HomeViewModel.class) {
                if (instance == null) {
                    instance = new HomeViewModel(context);
                }
            }
        }
        return instance;
    }
    public void logout(LogoutListener listener) {
        mAuth.signOut();
        listener.onLogoutComplete();
    }
    public interface LogoutListener {
        void onLogoutComplete();
    }
}
