package com.example.greenplate.decorators;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;

import com.example.greenplate.R;

public class FitnessDecorator extends HomeScreenDecorator {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String userUid;

    public FitnessDecorator(HomeScreenElement decoratedElement, Context context, String userUid) {
        super(decoratedElement);
        this.context = context;
        this.userUid = userUid;
        this.sharedPreferences = context.getSharedPreferences("FitnessPrefs_" + userUid, Context.MODE_PRIVATE);
    }


    public void display() {
        // Add fitness streak counter component to the home screen before displaying the decorated element
        // should have a button users can click to indicate that they have met their fitness for the day
                //and add to the streak
        // For example:
        // fitnessDisplay();
        // Call the display method of the decorated element
        super.display();
        fitnessDisplay(); // i call the display of fitness decorator
    }

    private void fitnessDisplay() {
        TextView streakView = ((Activity) context).findViewById(R.id.streakField);
        if (streakView == null) {
            streakView = new TextView(context);
            streakView.setId(R.id.streakField);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(streakView);
            addViewToHomeScreen(layout);
        }
        int currentStreak = sharedPreferences.getInt("streak", 0);
        streakView.setText(String.valueOf(currentStreak));
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        ConstraintLayout placeholder = ((Activity) context).findViewById(R.id.main_content);
        if (placeholder != null) {
            placeholder.addView(layout);
        } else {
            Log.e("FitnessDecorator", "Placeholder layout not found");
        }
    }
}
