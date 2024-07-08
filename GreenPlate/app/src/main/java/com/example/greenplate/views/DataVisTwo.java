package com.example.greenplate.views;

import com.example.greenplate.models.User;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.cartesian.series.Marker;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import com.example.greenplate.R;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataVisTwo extends AppCompatActivity {
    private MealActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

        Button toReturnButton = findViewById(R.id.btn_datavistwoback);
        viewModel = MealActivityViewModel.getInstance();
        PersonalActivityViewModel personalViewModel = PersonalActivityViewModel.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Monthly Activity: How well are you doing?");

        cartesian.yAxis(0).title("Calories");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        personalViewModel.getUserInfo(new PersonalActivityViewModel.UserInfoCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onUserInfoReceived(User user) {
                viewModel
                        .getEveryCalorieIntake(calorieList -> {
                            for (int i = 0; i < calorieList.size(); i++) {
                                if (calorieList.get(i) == 0) {
                                    seriesData.add(new CustomDataEntry("Day " + (i + 1), user
                                            .getCalorieGoal(), null));
                                } else {
                                    seriesData.add(new CustomDataEntry("Day " + (i + 1), user
                                            .getCalorieGoal(), calorieList.get(i)));
                                }
                            }

                            Set set = Set.instantiate();
                            set.data(seriesData);
                            Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                            Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

                            Line series1 = cartesian.line(series1Mapping);
                            series1.name("Goal");
                            int lineColor = ContextCompat.getColor(DataVisTwo.this, R.color.darkOlivine);
                            String lineColorHex = String.format("#%06X", (0xFFFFFF & lineColor));
                            series1.color(lineColorHex);
                            series1.hovered().markers().enabled(true);
                            series1.hovered().markers().type(MarkerType.CIRCLE).size(4d);
                            series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);

                            Marker series2 = cartesian.marker(series2Mapping);
                            series2.name("Monthly");
                            int markerColor = ContextCompat.getColor(DataVisTwo.this, R.color.olivine);
                            String markerColorHex = String.format("#%06X", (0xFFFFFF & markerColor));
                            series2.color(markerColorHex);
                            series2.hovered().markers().enabled(true);
                            series2.hovered().markers().type(MarkerType.CIRCLE).size(4d);
                            series2.tooltip().position("right").anchor(Anchor.LEFT_CENTER)
                                    .offsetX(5d)
                                    .offsetY(5d);
                            int backgroundColor = ContextCompat.getColor(DataVisTwo.this, R.color.beige);
                            String backgroundColorHex = String.format("#%06X", (0xFFFFFF & backgroundColor));
                            cartesian.background().fill(backgroundColorHex);
                            cartesian.legend().enabled(true);
                            cartesian.legend().fontSize(13d);
                            cartesian.legend().padding(0d, 0d, 10d, 0d);

                            anyChartView.setChart(cartesian);
                        });
            }
        });

        toReturnButton.setOnClickListener(v -> {
            Intent intent = new Intent(DataVisTwo.this, MealActivity.class);
            startActivity(intent);
        });

    }

    private static class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }

}