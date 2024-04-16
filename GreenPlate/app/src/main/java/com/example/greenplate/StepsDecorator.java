package com.example.greenplate;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepsDecorator extends HomeScreenDecorator {
    private Context context;
    private SharedPreferences sharedPreferences;
    private String userUid;

    public StepsDecorator(HomeScreenElement decoratedElement, Context context, String userUid) {
        super(decoratedElement);
        this.context = context;
        this.userUid = userUid;
    }

    public void display() {
        super.display();
        stepsDisplay();
    }

    private void stepsDisplay() {
        TextView stepsView = ((Activity) context).findViewById(R.id.stepsField);
        if (stepsView == null) {
            stepsView = new TextView(context);
            stepsView.setId(R.id.stepsField);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(stepsView);
            addViewToHomeScreen(layout);
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userUid).child("fitness").child(today).child("Steps");

        TextView finalStepsView = stepsView;
        stepsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int currentSteps = dataSnapshot.getValue(Integer.class);
                    finalStepsView.setText(String.valueOf(currentSteps));
                } else {
                    finalStepsView.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("StepsDecorator", "Failed to load steps", databaseError.toException());
            }
        });
    }

    private void addViewToHomeScreen(LinearLayout layout) {
        ConstraintLayout placeholder = ((Activity) context).findViewById(R.id.main_content);
        if (placeholder != null) {
            placeholder.addView(layout);
        } else {
            Log.e("StepsDecorator", "Placeholder layout not found");
        }
    }
}
