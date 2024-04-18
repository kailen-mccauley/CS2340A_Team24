package com.example.greenplate.decorators;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;

import com.example.greenplate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityDecorator extends HomeScreenDecorator {
    private Context context;
    private String userUid;

    public ActivityDecorator(HomeScreenElement decoratedElement, Context context, String userUid) {
        super(decoratedElement);
        this.context = context;
        this.userUid = userUid;
    }

    public void display() {
        super.display();
        activityDisplay();
    }

    private void activityDisplay() {
        TextView timeView = ((Activity) context).findViewById(R.id.activityField);
        if (timeView == null) {
            timeView = new TextView(context);
            timeView.setId(R.id.activityField);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(timeView);
            addViewToHomeScreen(layout);
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference activitiesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid).child("fitness").child(today).child("Activity");

        TextView finalTimeView = timeView;
        activitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long totalTimeInSeconds = 0;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                    String activityTime = activitySnapshot.getValue(String.class);
                    try {
                        Date activityDuration = timeFormat.parse(activityTime);
                        totalTimeInSeconds += (activityDuration.getHours() * 3600) +
                                (activityDuration.getMinutes() * 60) +
                                activityDuration.getSeconds();
                    } catch (ParseException e) {
                        Log.e("TimeDecorator", "Failed to parse activity time", e);
                    }
                }

                int hours = (int) totalTimeInSeconds / 3600;
                int minutes = (int) (totalTimeInSeconds % 3600) / 60;
                int seconds = (int) totalTimeInSeconds % 60;

                String totalTimeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                finalTimeView.setText(String.valueOf(totalTimeFormatted));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TimeDecorator", "Failed to load activities", databaseError.toException());
            }
        });
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
