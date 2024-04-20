package com.example.greenplate.utilites;

import android.text.SpannableString;
import java.util.Set;
public class InputFormatter {
    public static String capitalize(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
    public static SpannableString capitalize(SpannableString str) {
        String originalString = str.toString();
        String capitalizedString = originalString.substring(0, 1).toUpperCase() + originalString.substring(1);
        return new SpannableString(capitalizedString);
    }


    public static String capitalize(Set<SpannableString> set) {
        for (SpannableString spannableString: set) {
            String str = spannableString.toString();
            str = str.substring(0,1).toUpperCase() + str.substring(1);
        }
        return null;
    }


    public static String lowercase(String str) {
        return str.toLowerCase();
    }
}
