package com.example.elhamadamo.gotcha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class get_activity extends AppCompatActivity {

    TextView view1;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_activity);
        create = (Button) findViewById(R.id.create_product);
        view1 = (TextView) findViewById(R.id.view1);
        new Handler().execute();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),post_activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    class Handler extends AsyncTask<Void,Void,Void> {

        String a = "";

        @Override
        protected Void doInBackground(Void... params) {
            //GET METHOD
            HttpURLConnection c = null;
            try{
                String url = "http://localhost/gotcha/get_products.php";
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(),"iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line = br.readLine()) != null){
                    sb.append(line+"\n");
                }
                br.close();
                String temp = sb.toString();

                JSONObject jo = new JSONObject(temp);
                JSONArray ja = jo.getJSONArray("products");
                for (int i=0; i < ja.length(); i++)
                {
                    try {
                        JSONObject j = ja.getJSONObject(i);
                        // Pulling items from the array
                        String i1 = j.getString("pid");
                        String i2 = j.getString("name");
                        String i3 = j.getString("price");
                        a += i1 + i2 + i3 + "\n";
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            }catch(Exception e){
                System.out.println(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view1.setText(a);
                }
            });
        }
    }

}
