package com.example.elhamadamo.gotcha_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import conf.AppConfig;
import conf.AppController;
import s_handler.SQLiteHandler;
import s_handler.SessionManager;

public class FrontPageActivity extends AppCompatActivity {

    //Session
    private TextView txtItem;
    private Button btnLogout;

    private SQLiteHandler db;
    private SessionManager session;
    private static final String TAG = FrontPageActivity.class.getSimpleName();

    //List of items
    private ArrayList<String[]> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        txtItem = (TextView) findViewById(R.id.item);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if(!session.isLoggedIn()){
            logoutUser();
        }

        getList();

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
                    JSONObject jObj = new JSONObject(response);
                    JSONArray ja = jObj.getJSONArray("products");
                    String values = "";
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
                            values += a;
                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    txtItem.setText(values);
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

        Intent intent = new Intent(FrontPageActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
