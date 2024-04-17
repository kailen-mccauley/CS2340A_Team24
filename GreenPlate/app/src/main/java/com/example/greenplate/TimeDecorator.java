package com.example.greenplate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeDecorator extends HomeScreenDecorator {
    private Context context;

    public TimeDecorator(HomeScreenElement decoratedElement, Context context) {
        super(decoratedElement);
        this.context = context;
    }

    public void display() {
        super.display();
        timeDisplay();
    }

    private void timeDisplay() {
        TextView timeView = ((Activity) context).findViewById(R.id.timeField);
        if (timeView == null) {
            timeView = new TextView(context);
            timeView.setId(R.id.timeField);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(timeView);
            addViewToHomeScreen(layout);
        }

        String currentTime = new SimpleDateFormat("HH:mm:ss",
                Locale.getDefault()).format(new Date());
        timeView.setText(currentTime);
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        ConstraintLayout placeholder = ((Activity) context).findViewById(R.id.main_content);
        if (placeholder != null) {
            placeholder.addView(layout);
        } else {
            Log.e("TimeDecorator", "Placeholder layout not found");
        }
    }
}
