package com.example.greenplate.views;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.greenplate.R;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;
import com.example.greenplate.R.color;

import java.util.ArrayList;
import java.util.List;

public class DataVisOne extends AppCompatActivity {
    private MealActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Button toReturnButton = findViewById(R.id.btn_datavisoneback);
        viewModel = MealActivityViewModel.getInstance();
        PersonalActivityViewModel personalViewModel = PersonalActivityViewModel.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        personalViewModel.getUserInfo(user ->  {
            viewModel.getDailyCalorieIntake(new MealActivityViewModel.
                        DailyCalorieIntakeCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDailyCalorieIntakeReceived(int totalCalories) {
                        viewModel
                                .getAverageCalorieIntake(averageCalories -> {
                                    data.add(new ValueDataEntry("Today", totalCalories));
                                    data.add(new ValueDataEntry("Goal", user.getCalorieGoal()));
                                    data.add(new ValueDataEntry("Average", averageCalories));

                                    Column column = cartesian.column(data);
                                    column.tooltip()
                                        .titleFormat("{%X}")
                                        .position(Position.CENTER_BOTTOM)
                                        .anchor(Anchor.CENTER_BOTTOM)
                                        .offsetX(0d)
                                        .offsetY(5d)
                                            .format("{%Value}{groupsSeparator: }");
                                    int barColor = ContextCompat.getColor(DataVisOne.this, R.color.olivine);
                                    String barColorHex = String.format("#%06X", (0xFFFFFF & barColor));
                                    column.fill(barColorHex);
                                    column.stroke(barColorHex);
                                    int backgroundColor = ContextCompat.getColor(DataVisOne.this, R.color.beige);
                                    String backgroundColorHex = String.format("#%06X", (0xFFFFFF & backgroundColor));
                                    cartesian.background().fill(backgroundColorHex);
                                    cartesian.animation(true);
                                    cartesian.title("Today Vs Goal");

                                    cartesian.yScale().minimum(0d);

                                    cartesian.yAxis(0).labels()
                                            .format("{%Value}{groupsSeparator: }");

                                    cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                                    cartesian.interactivity().hoverMode(HoverMode.BY_X);

                                    cartesian.xAxis(0).title("Intake");
                                    cartesian.yAxis(0).title("Calories");

                                    anyChartView.setChart(cartesian);
                                });
                    }
                });
        });

        toReturnButton.setOnClickListener(v ->  {
            Intent intent = new Intent(DataVisOne.this, MealActivity.class);
            startActivity(intent);
        });

    }
}