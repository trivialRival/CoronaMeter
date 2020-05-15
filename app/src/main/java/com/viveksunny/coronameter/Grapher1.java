package com.viveksunny.coronameter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Grapher1 extends AppCompatActivity  {
    private LineChart chart;
    String[] casenums;
    Intent intent;
    GraphView graphView;
    float x,y;
    LineGraphSeries<DataPoint> series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_grapher1);
        intent=getIntent();
        casenums=intent.getStringArrayExtra("Gseries");
        series=new LineGraphSeries<DataPoint>();
        graphView=(GraphView)findViewById(R.id.Gview2);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(8);
        series.setThickness(6);
        series.setColor(Color.rgb(20,148,20));
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int d=(int) Math.round(dataPoint.getX());
                int c=(int)Math.round(dataPoint.getY());
                Toast.makeText(Grapher1.this, "Cases at day "+d+" is "+c, Toast.LENGTH_SHORT).show();
            }
        });
        for(int i=0;i<casenums.length;i++){
            x= i+1;
            y=Integer.valueOf(casenums[i]);
            series.appendData(new DataPoint(x,y),true,150);
        }

        graphView.setTitle("CASES");
        graphView.setTitleColor(Color.rgb(20,148,20));
        GridLabelRenderer gridlabel= graphView.getGridLabelRenderer();
        gridlabel.setGridColor(Color.rgb(20,148,20));
        gridlabel.setVerticalAxisTitleColor(Color.rgb(20,148,20));
        gridlabel.setVerticalLabelsColor(Color.rgb(20,148,20));
        gridlabel.setHorizontalAxisTitle("days");
        gridlabel.setVerticalAxisTitle("Cases");
        graphView.addSeries(series);
    }
}
