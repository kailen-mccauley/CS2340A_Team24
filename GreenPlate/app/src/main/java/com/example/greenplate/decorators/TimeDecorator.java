package com.example.greenplate.decorators;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.greenplate.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeDecorator extends HomeScreenDecorator {
    private Context context;
    private Handler timerHandler = new Handler();
    private int seconds = 0;

    private TextView timeView;

    public TimeDecorator(HomeScreenElement decoratedElement, Context context) {
        super(decoratedElement);
        this.context = context;
    }

    public void display() {
        super.display();
        timeDisplay();
    }

    private void timeDisplay() {
        timeView = ((Activity) context).findViewById(R.id.timeField);
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
        seconds = getSeconds(currentTime);
        runTimer();
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        ConstraintLayout placeholder = ((Activity) context).findViewById(R.id.main_content);
        if (placeholder != null) {
            placeholder.addView(layout);
        } else {
            Log.e("TimeDecorator", "Placeholder layout not found");
        }
    }
    private void runTimer() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds++;
                timerHandler.post(() -> timeView.setText(formatTime(seconds)));
            }
        }).start();
    }

    private int getSeconds(String time) {
        String[] brokenDownTime = time.split(":");
        int hoursToSec = Integer.parseInt(brokenDownTime[0]) * 3600;
        int minutesToSec = Integer.parseInt(brokenDownTime[1]) * 60;
        int secs = Integer.parseInt(brokenDownTime[2]);
        return hoursToSec + minutesToSec + secs;
    }

    private String formatTime(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int secs = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
