package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import androidx.appcompat.app.AppCompatActivity;

import com.example.greenplate.R;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataVisOne extends AppCompatActivity {
    private Button toReturnButton;
    private MealActivityViewModel viewModel;
    private PersonalActivityViewModel personalViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        toReturnButton = findViewById(R.id.btn_datavisoneback);
        viewModel = MealActivityViewModel.getInstance();
        personalViewModel = PersonalActivityViewModel.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        personalViewModel.getUserInfo(new PersonalActivityViewModel.UserInfoCallback() {
            @Override
            public void onUserInfoReceived(PersonalActivityViewModel.User user) {
                viewModel.getDailyCalorieIntake(new MealActivityViewModel.DailyCalorieIntakeCallback() {
                    @Override
                    public void onDailyCalorieIntakeReceived(int totalCalories) {
                        data.add(new ValueDataEntry("Today", totalCalories));
                        data.add(new ValueDataEntry("Goal", user.getCalorieGoal()));

                        Column column = cartesian.column(data);

                        column.tooltip()
                                .titleFormat("{%X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("${%Value}{groupsSeparator: }");

                        cartesian.animation(true);
                        cartesian.title("Today Vs Goal");

                        cartesian.yScale().minimum(0d);

                        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.xAxis(0).title("Intake");
                        cartesian.yAxis(0).title("Calories");

                        anyChartView.setChart(cartesian);
                    }
                });
            }
        });

//        data.add(new ValueDataEntry("Today", 1220));
//        data.add(new ValueDataEntry("Goal", 2000));
//        data.add(new ValueDataEntry("Mascara", 200));
//        data.add(new ValueDataEntry("Lip gloss", 3200));
//        data.add(new ValueDataEntry("Lipstick", 1000));
//        data.add(new ValueDataEntry("Nail polish", 890));
//        data.add(new ValueDataEntry("Eyebrow pencil", 750));
//        data.add(new ValueDataEntry("Eyeliner", 1050));
//        data.add(new ValueDataEntry("Eyeshadows", 500));

//        Column column = cartesian.column(data);
//
//        column.tooltip()
//                .titleFormat("{%X}")
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(5d)
//                .format("${%Value}{groupsSeparator: }");
//
//        cartesian.animation(true);
//        cartesian.title("Today Vs Goal");
//
//        cartesian.yScale().minimum(0d);
//
//        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
//
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//        cartesian.interactivity().hoverMode(HoverMode.BY_X);
//
//        cartesian.xAxis(0).title("Intake");
//        cartesian.yAxis(0).title("Calories");
//
//        anyChartView.setChart(cartesian);

        toReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataVisOne.this, MealActivity.class);
                startActivity(intent);
            }
        });

    }
}