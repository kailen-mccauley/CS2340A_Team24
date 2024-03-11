package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import com.example.greenplate.R;
import com.example.greenplate.viewmodels.MealActivityViewModel;
import com.example.greenplate.viewmodels.PersonalActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataVisTwo extends AppCompatActivity {
    private Button toReturnButton;
    private MealActivityViewModel viewModel;
    private PersonalActivityViewModel personalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

        toReturnButton = findViewById(R.id.btn_datavistwoback);
        viewModel = MealActivityViewModel.getInstance();
        personalViewModel = PersonalActivityViewModel.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Monthly Activity: How well are you doing?");

        cartesian.yAxis(0).title("Calories");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        personalViewModel.getUserInfo(new PersonalActivityViewModel.UserInfoCallback() {
            @Override
            public void onUserInfoReceived(PersonalActivityViewModel.User user) {
                viewModel.getEveryCalorieIntake(new MealActivityViewModel.EveryCalorieIntakeCallback() {
                    @Override
                    public void onEveryCalorieIntakeReceived(List<Integer> calorieList) {
                        for (int i = 0; i < calorieList.size(); i++) {
                            seriesData.add(new CustomDataEntry("Day "+(i+1), user.getCalorieGoal(), calorieList.get(i)));
                        }

                        Set set = Set.instantiate();
                        set.data(seriesData);
                        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

                        Line series1 = cartesian.line(series1Mapping);
                        series1.name("Goal");
                        series1.hovered().markers().enabled(true);
                        series1.hovered().markers()
                                .type(MarkerType.CIRCLE)
                                .size(4d);
                        series1.tooltip()
                                .position("right")
                                .anchor(Anchor.LEFT_CENTER)
                                .offsetX(5d)
                                .offsetY(5d);

                        Line series2 = cartesian.line(series2Mapping);
                        series2.name("Monthly");
                        series2.hovered().markers().enabled(true);
                        series2.hovered().markers()
                                .type(MarkerType.CIRCLE)
                                .size(4d);
                        series2.tooltip()
                                .position("right")
                                .anchor(Anchor.LEFT_CENTER)
                                .offsetX(5d)
                                .offsetY(5d);

                        cartesian.legend().enabled(true);
                        cartesian.legend().fontSize(13d);
                        cartesian.legend().padding(0d, 0d, 10d, 0d);

                        anyChartView.setChart(cartesian);
                    }
                });
            }
        });

        toReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataVisTwo.this, MealActivity.class);
                startActivity(intent);
            }
        });

    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }

    }

}