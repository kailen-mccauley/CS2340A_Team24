package com.example.greenplate;

public class CreateAccountViewModel {
    private static CreateAccountViewModel instance;
    public static synchronized CreateAccountViewModel getInstance() {
        if (instance == null) {
            instance = new CreateAccountViewModel();
        }
        return instance;
    }

    public boolean isValidUsernameOrPassword(String username, String password) {
        return username != null && !username.contains(" ") && !username.isEmpty()
                && password != null && !password.contains(" ") && !password.isEmpty();
    }
}
