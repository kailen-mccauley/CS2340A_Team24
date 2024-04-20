package com.example.greenplate.utilites;

public class InputFormatter {
    public static String capitalize(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    public static String lowercase(String str) {
        return str.toLowerCase();
    }
}
