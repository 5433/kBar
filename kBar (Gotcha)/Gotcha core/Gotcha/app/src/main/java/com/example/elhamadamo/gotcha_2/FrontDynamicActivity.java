package com.example.elhamadamo.gotcha_2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import conf.AppConfig;
import conf.AppController;
import s_handler.SQLiteHandler;
import s_handler.SessionManager;

public class FrontDynamicActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //Session
    private Button btnLogout;
    private Button postButton;
    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = FrontDynamicActivity.class.getSimpleName();

    //List of items
    private ArrayList<String[]> itemList = new ArrayList<>();

    //Google Client
    private GoogleApiClient gac;
    private Location lastLocation;

    //Location variables
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_dynamic);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        postButton = (Button) findViewById(R.id.postButton);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn()){
            logoutUser();
        }

        getList();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontDynamicActivity.this,PostActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void getList(){
        String tag_string_req = "req_list";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GET_LIST,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d(TAG, "Register Response_______: " + response.toString());
                try{
                    LinearLayout main_layer= (LinearLayout) findViewById(R.id.dynamic_layout);
                    JSONObject jObj = new JSONObject(response);
                    JSONArray ja = jObj.getJSONArray("products");
                    for (int i=0; i < ja.length(); i++)
                    {
                        try {
                            JSONObject j = ja.getJSONObject(i);
                            // Pulling items from the array
                            String data[] = new String[3];
                            data[0] = j.getString("pid");
                            data[1] = j.getString("name");
                            data[2] = j.getString("price");
                            itemList.add(data);
                            String a = data[0] + data[1] + data[2] + "\n";

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                            LinearLayout layout = new LinearLayout(getApplicationContext());
                            //LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setLayoutParams(params);
                            Button button1 = new Button(getApplicationContext());
                            //params1.gravity = Gravity.CENTER;
                            //button1.setLayoutParams(params1);
                            params.gravity = Gravity.CENTER;
                            button1.setLayoutParams(params);
                            button1.setText(a);

                            layout.addView(button1);
                            main_layer.addView(layout);

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG,"Registration error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<>();
//                for(int i=0;i < itemList.size();i++){
//                    String temp [] = itemList.get(i);
//                    params.put("pid",temp[0]);
//                    params.put("email",temp[1]);
//                    params.put("password",temp[2]);
//                }
//                return params;
//
//            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void logoutUser(){
        session.setLogin(false);

        db.deleteUser();

        Intent intent = new Intent(FrontDynamicActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startGAC(){
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS){
            gacBuilder();
        }else{
            Log.e(TAG,"Cannot connect to Google Services");
        }
    }

    protected synchronized void gacBuilder(){
        gac = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if(!gac.isConnected() || !gac.isConnecting()){
            gac.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(lastLocation != null){
            longitude = lastLocation.getLongitude();
            latitude = lastLocation.getLatitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

