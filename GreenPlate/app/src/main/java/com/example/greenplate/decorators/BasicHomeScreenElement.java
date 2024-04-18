package com.example.greenplate.decorators;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BasicHomeScreenElement implements HomeScreenElement {
    private Context context;

    public BasicHomeScreenElement(Context context) {
        this.context = context;
    }

    @Override
    public void display() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(context);
        textView.setText("Welcome to Your Home Screen!");
        layout.addView(textView);

        addViewToHomeScreen(layout);
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        // this is what i used to add layout to the homeactivity but honeslty might exist a better way to do it
    }
}
