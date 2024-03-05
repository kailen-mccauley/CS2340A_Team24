package com.example.greenplate.views;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
import com.google.firebase.Firebase;

import java.util.ArrayList;
import java.util.List;

public class DataVisOne extends AppCompatActivity {

//    private MealActivityViewModel viewModel;
    private Button toReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // DO NOT MODIFY
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        toReturnButton = findViewById(R.id.btn_datavisback);

//        viewModel = MealActivityViewModel.getInstance();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

//        for (int day = 1; day <= 30; day++) {
//            final int currentDay = day;
//
//            //viewModel.queryUserMeals(currentDay, new MealActivityViewModel.MealDataCallback() {
//            viewModel.queryUserMeals() {
//                @Override
//                public void onMealDataReceived(int day, int calories) {
//                    data.add(new ValueDataEntry("Day " + day, calories));
//                }
//            });
//        }

        data.add(new ValueDataEntry("Rouge", 1220));
        data.add(new ValueDataEntry("Foundation", 4000));
        data.add(new ValueDataEntry("Mascara", 200));
        data.add(new ValueDataEntry("Lip gloss", 3200));
        data.add(new ValueDataEntry("Lipstick", 1000));
        data.add(new ValueDataEntry("Nail polish", 890));
        data.add(new ValueDataEntry("Eyebrow pencil", 750));
        data.add(new ValueDataEntry("Eyeliner", 1050));
        data.add(new ValueDataEntry("Eyeshadows", 500));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");

        anyChartView.setChart(cartesian);

        toReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataVisOne.this, MealActivity.class);
                startActivity(intent);
            }
        });

    }
}