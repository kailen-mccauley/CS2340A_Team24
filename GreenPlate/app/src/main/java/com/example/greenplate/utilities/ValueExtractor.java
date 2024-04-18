package com.example.greenplate.utilities;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;

import com.example.greenplate.models.Ingredient;

import java.util.TreeMap;

public class ValueExtractor {
    public static String extract(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }
    public static String extract(EditText editText) {
        return editText.getText().toString();
    }
    public static String extract(TextView textView) {
        return textView.getText().toString();
    }

    public static Ingredient extract(TreeMap<String, Ingredient> treeMap, String key) {
        return treeMap.get(key);
    }
}
