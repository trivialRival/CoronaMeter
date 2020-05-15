package com.viveksunny.coronameter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Splashy extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splashy);
        progressBar=findViewById(R.id.progressBar);
        progressBar.isIndeterminate();

        new doit().execute("https://www.worldometers.info/coronavirus/");
    }
    class doit extends AsyncTask<String,Integer,Void> {
        String words2="";
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
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            ArrayList<String> countrylinks= new ArrayList<String>();
            ArrayList<String> clinks= new ArrayList<String>();
            ArrayList<String> cnames= new ArrayList<String>();
            int pter=0;int j=0;int r=0;
            String str="";
            countrylinks.removeAll(countrylinks);

            clinks.removeAll(clinks);
            for(int i=0;i<419;i++){
                countrylinks.add(words2.substring(words2.indexOf("country", words2.indexOf("class=\"mt_a\" href",pter)), words2.indexOf("</a></td>", words2.indexOf("class=\"mt_a\" href",pter))));
                pter= words2.indexOf("</a></td>", words2.indexOf("class=\"mt_a\" href",pter))+50;
            }
            for(int i=0;i<countrylinks.size();i++){
                clinks.add("https://www.worldometers.info/coronavirus/"+countrylinks.get(i).substring(0,countrylinks.get(i).indexOf("\"")-1));
                cnames.add(countrylinks.get(i).substring(countrylinks.get(i).indexOf(">")+1));
            }
            for(int i=0;i<clinks.size();i++){
                System.out.println(cnames.get(i));
                System.out.println(clinks.get(i));
            }
            System.out.println("check");
            Intent intent;
            intent=new Intent(Splashy.this,MainActivity.class);
            intent.putStringArrayListExtra("clinks",clinks);
            intent.putStringArrayListExtra("cnames",cnames);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
