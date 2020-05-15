package com.viveksunny.coronameter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    String words2="";
    String cases="";String Deaths="";String recovered="";String cvalues="";
    Button button1,button2,button3;
    TextView texx;int fuse;
    ProgressBar progressBar;
    Spinner spinner;Intent intent;
    ArrayList<String> clinks= new ArrayList<String>();
    ArrayList<String> cnames= new ArrayList<String>();
    ArrayAdapter<String> adapter;
    MediaPlayer Player3;
    int pos;
    GraphView graph;
    public void nextSound(){
        if(Player3==null){
            Player3= MediaPlayer.create(this,R.raw.nextbutt);
        }
        Player3.start();
    }
    boolean internet_connection(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        button1=findViewById(R.id.button3);
        button2=findViewById(R.id.button4);
        button3=findViewById(R.id.button5);
        progressBar =findViewById(R.id.pbar);
        progressBar.isIndeterminate();
        progressBar.setAlpha(0f);
        intent=getIntent();
        clinks=intent.getStringArrayListExtra("clinks");
        cnames=intent.getStringArrayListExtra("cnames");
        fuse=0;
        spinner= findViewById(R.id.spinner);
        if(!internet_connection()){
            Toast.makeText(this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        adapter= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cnames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                new doit2().execute(clinks.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                finish();
            }
        });
    }

    class doit2 extends AsyncTask<String,Integer,Void>{
        Vector V = new Vector();
        Bitmap myimage;
        @Override
        protected Void doInBackground(String... urls) {
            try {
                Document doc= Jsoup.connect(urls[0]).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
                words2 = doc.html();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            System.out.println("doing");
            progressBar.setAlpha(1f);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setAlpha(0f);
            int temp= words2.indexOf("Coronavirus Cases");
            int temp2= words2.indexOf("<h1>Deaths");
            int temp3= words2.indexOf("<h1>Recovered");
            String cvstr="";
            String Lupdated;
            String flag;
            ImageView imageView=findViewById(R.id.imageView2);
            ImageDownloader task = new ImageDownloader();
            flag="https://www.worldometers.info/"+words2.substring(words2.indexOf("img/flags"),words2.indexOf(".gif",words2.indexOf("img/flags"))+4);
            try {
                myimage=task.execute(flag).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(myimage);
            if(!internet_connection()){
                Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
            cases= words2.substring(words2.indexOf('>', words2.indexOf("color:#aaa",temp))+1 , words2.indexOf('<', words2.indexOf("color:#aaa",temp)));
            Deaths= words2.substring(words2.indexOf("span>",temp2)+5 , words2.indexOf("</span",temp2));
            recovered= words2.substring(words2.indexOf("span>",temp3)+5 , words2.indexOf("</span",temp3));
            button1.setText("Cases: "+cases);
            button2.setText("Deaths: "+Deaths);
            button3.setText("Recovered: "+recovered);
            texx=findViewById(R.id.textView);
            Lupdated= words2.substring(words2.indexOf("Last updated:"), words2.indexOf("GMT", words2.indexOf("Last updated:"))-8);
            texx.setText(Lupdated);
            int gpoint = words2.indexOf("data", words2.indexOf("coronavirus-cases-linear"));
            cvalues = words2.substring(words2.indexOf("data:",gpoint)+2, words2.indexOf(']', words2.indexOf("data:",gpoint)+5))+",";
            final String [] casenums;
            System.out.println(cvstr);
            cvstr=cvalues.substring(5);
            Vector V=new Vector();
            casenums=cvstr.split(",");
            LineGraphSeries<DataPoint> series;
            graph= findViewById(R.id.Gview2);
            series= new LineGraphSeries<>();
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(8);
            series.setThickness(6);
            series.setColor(Color.rgb(20,148,20));
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    int d=(int) Math.round(dataPoint.getX());
                    int c=(int)Math.round(dataPoint.getY());
                    Toast.makeText(MainActivity.this, "Cases at day "+d+" is "+c, Toast.LENGTH_SHORT).show();
                }
            });
            graph.removeAllSeries();
            int x,y;
            for(int i=0;i<casenums.length-1;i++){
                x=i;
                y=Integer.valueOf(casenums[i+1])-Integer.valueOf(casenums[i]);
                series.appendData(new DataPoint(x,y),true,60);
            }
            graph.addSeries(series);
            boolean Us2=false;
//            boolean Us3=false;
            graph.setTitle("New cases per day");
            graph.setTitleColor(Color.rgb(20,148,20));
            GridLabelRenderer gridlabel= graph.getGridLabelRenderer();
            gridlabel.setGridColor(Color.rgb(20,148,20));
            gridlabel.setVerticalAxisTitle("Cases for day in "+ cnames.get(pos));
            gridlabel.setHorizontalAxisTitle("Days");
            gridlabel.setHorizontalAxisTitleColor(Color.rgb(20,148,20));
            gridlabel.setVerticalAxisTitleColor(Color.rgb(20,148,20));
            gridlabel.setVerticalLabelsColor(Color.rgb(20,148,20));
//            Toast.makeText(MainActivity.this, cnames.get(pos), Toast.LENGTH_SHORT).show();
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextSound();
                    Intent intent=new Intent(MainActivity.this,Grapher1.class);
                    intent.putExtra("Gseries",casenums);
                    startActivity(intent);
                }
            });
            final boolean finalUs = Us2;
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cnames.get(pos).indexOf("USA")!=-1) {
                        Toast.makeText(MainActivity.this, "Data Unavailable", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        nextSound();
                        Intent intent2 = new Intent(getApplicationContext(), Gpr2.class);
                        intent2.putExtra("html", words2);
//                    Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();
                        startActivity(intent2);
                    }
                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cnames.get(pos).indexOf("USA")!=-1) {
                        Toast.makeText(MainActivity.this, "Data Unavailable", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        nextSound();
                        Intent intent3 = new Intent(getApplicationContext(), Gpr3.class);
                        intent3.putExtra("html", words2);
                        startActivity(intent3);
                    }
                }
            });
        }
    }
    public class ImageDownloader extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url= new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap btmp= BitmapFactory.decodeStream(in);
                return btmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
