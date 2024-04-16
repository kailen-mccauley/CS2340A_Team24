package com.example.greenplate;

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

        // This method should actually add this view to the home activity's layout.
        // You will need to ensure this layout is added to your activity's view hierarchy.
        addViewToHomeScreen(layout);
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        // Code to add this layout to the HomeActivity's content view
    }
}
