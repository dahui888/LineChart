package com.example.linechart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import chart.LineChartView;

public class MainActivity extends AppCompatActivity {

    LineChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = (LineChartView) findViewById(R.id.chart_view);
        List<String> keys = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            keys.add(i < 10 ? "0" + i : "" + i);
        }
        chartView.setKeys(keys);



        List<Double> values = new ArrayList<>();
        values.add(Double.valueOf(2334.12938383));
        values.add(Double.valueOf(833.14));
        values.add(Double.valueOf(1452.56));
        values.add(Double.valueOf(223.14));
        values.add(Double.valueOf(631.14));
        values.add(Double.valueOf(23.14));
        values.add(Double.valueOf(563.14));
        values.add(Double.valueOf(1523.14));
        values.add(Double.valueOf(223.14));
        values.add(Double.valueOf(790.14));
        values.add(Double.valueOf(23.14));
        values.add(Double.valueOf(1934.1342));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
//        values.add(Double.valueOf(223.14));
        chartView.setValues(values);

        chartView.work();
    }
}
