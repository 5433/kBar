package com.example.elhamadamo.gotcha;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class post_activity extends AppCompatActivity {

    EditText priceField;
    EditText nameField;
    Button send;
    TextView txt;
    String response = "";
    String name = "";
    String price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activity);
        priceField = (EditText) findViewById(R.id.price);
        nameField = (EditText) findViewById(R.id.name);
        send = (Button) findViewById(R.id.send);
        txt = (TextView) findViewById(R.id.textView);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = priceField.getText().toString();
                name = nameField.getText().toString();
                new Handler().execute();
            }
        });
    }

    class Handler extends AsyncTask<Void,Void,Void> {

        HttpURLConnection con;

        @Override
        protected Void doInBackground(Void... params) {
            try{
                String data = "name="+URLEncoder.encode(name,"UTF-8")+"&price="+URLEncoder.encode(price,"UTF-8");
                URL url = new URL("http://localhost/gotcha/create_p1.php");
                con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(data.getBytes().length);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                PrintWriter pw = new PrintWriter(con.getOutputStream());
                pw.write(data);
                pw.flush();
                pw.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try {
                Scanner s = new Scanner(con.getInputStream());
                while(s.hasNextLine()){
                    response += (s.nextLine());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt.setText(response);
                    if(response.contains("1")){
                        Intent i = new Intent(getApplicationContext(),get_activity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }
}
