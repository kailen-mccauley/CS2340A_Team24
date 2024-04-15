package com.example.greenplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FitnessDecorator extends HomeScreenDecorator {
    private Context context;
    private SharedPreferences sharedPreferences;

    public FitnessDecorator(HomeScreenElement decoratedElement, Context context) {
        super(decoratedElement);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("FitnessPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void display() {
        // Add fitness streak counter component to the home screen before displaying the decorated element
        // should have a button users can click to indicate that they have met their fitness for the day
                //and add to the streak
        // For example:
        // fitnessDisplay();
        // Call the display method of the decorated element
        super.display();
    }

    private void fitnessDisplay() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView streakView = new TextView(context);
        int currentStreak = sharedPreferences.getInt("streak", 0);
        streakView.setText("Current Streak: " + currentStreak);
        layout.addView(streakView);

        Button fitnessButton = new Button(context);
        fitnessButton.setText("Log Fitness");
        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newStreak = currentStreak + 1;
                sharedPreferences.edit().putInt("streak", newStreak).apply();
                streakView.setText("Current Streak: " + newStreak);
                Toast.makeText(context, "Fitness logged!", Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(fitnessButton);

        addViewToHomeScreen(layout);
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        // this is where i need UI to test but not sure how
        // implementation should work tho
    }
}
