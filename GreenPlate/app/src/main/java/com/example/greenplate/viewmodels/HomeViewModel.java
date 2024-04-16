package com.example.greenplate.viewmodels;

import android.content.SharedPreferences;
import android.content.Context;

public class HomeViewModel {

    private static volatile HomeViewModel instance;
    private SharedPreferences sharedPreferences;

    private HomeViewModel(Context context) {
        sharedPreferences = context.getSharedPreferences("FitnessPrefs", Context.MODE_PRIVATE);
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
}
