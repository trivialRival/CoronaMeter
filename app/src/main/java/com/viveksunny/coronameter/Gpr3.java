package com.viveksunny.coronameter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

public class Gpr3 extends AppCompatActivity {
    String dvalstr="";
    GraphView graphView2;
    LineGraphSeries<DataPoint> series2;
    String html="";
    String words="";
    String[] dvalues;
    int Spoint;
    int x2,y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gpr3);
        graphView2=findViewById(R.id.Gvieww);
        series2=new LineGraphSeries<DataPoint>();
        Intent intent= getIntent();
        html=intent.getStringExtra("html");
        words=html;
        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(8);
        series2.setThickness(6);
        series2.setColor(Color.rgb(20,148,20));
        series2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int d=(int) Math.round(dataPoint.getX());
                int c=(int)Math.round(dataPoint.getY());
                Toast.makeText(Gpr3.this, "Deaths at day "+d+" is "+c, Toast.LENGTH_SHORT).show();
            }
        });
        if(words.indexOf("name: 'New Recoveries',")!=-1) {
            Spoint = words.indexOf("data: [", words.indexOf("name: 'New Recoveries',"));
            dvalstr = words.substring(words.indexOf('[', Spoint) + 1, words.indexOf(']', Spoint)) + ",";
            dvalues = dvalstr.split(",");
            for (int i = 0; i < dvalues.length; i++) {
                x2 = i + 1;
                y2 = Integer.valueOf(dvalues[i]);
                series2.appendData(new DataPoint(x2, y2), true, 150);
            }
            graphView2.setTitle("Recoveries per day");
            graphView2.setTitleColor(android.R.color.holo_green_light);
            GridLabelRenderer gridlabel = graphView2.getGridLabelRenderer();
            gridlabel.setHorizontalAxisTitle("days");
            gridlabel.setVerticalAxisTitle("No of Recoveries/Day");
            gridlabel.setGridColor(Color.rgb(20,148,20));
            gridlabel.setVerticalAxisTitleColor(Color.rgb(20,148,20));
            gridlabel.setVerticalLabelsColor(Color.rgb(20,148,20));
            graphView2.addSeries(series2);
        }
        else {
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
