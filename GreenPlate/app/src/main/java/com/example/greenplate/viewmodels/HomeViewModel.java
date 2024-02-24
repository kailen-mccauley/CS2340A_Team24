package com.example.greenplate.viewmodels;

public class HomeViewModel {
    private static HomeViewModel instance;
    public static synchronized HomeViewModel getInstance() {
        if (instance == null) {
            instance = new HomeViewModel();
        }
        return instance;
    }
}
