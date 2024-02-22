package com.example.greenplate;

public class LoginViewModel {
    private static LoginViewModel instance;
    public static synchronized LoginViewModel getInstance() {
        if (instance == null) {
            instance = new LoginViewModel();
        }
        return instance;
    }

    public boolean isValidUsernameOrPassword(String username, String password) {
        return (username != null && !username.contains(" ") && !username.isEmpty())
                && (password != null && !password.contains(" ") && !password.isEmpty());
    }
}
