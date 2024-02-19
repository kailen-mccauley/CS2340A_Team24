package com.example.greenplate;

public class HomeViewModel {
    private static HomeViewModel instance;
    public static synchronized HomeViewModel getInstance() {
        if (instance == null) {
            instance = new HomeViewModel();
        }
        return instance;
    }
}
