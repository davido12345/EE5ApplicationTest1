package com.example.david.ee5application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Keys;
import com.example.david.ee5application.Databases.Links;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Driver;

public class Upload_page extends AppCompatActivity {
    public String TAG = "UPLOAD PAGE:";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_page);

        progressBar = findViewById(R.id.Progress);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        Intent start = getIntent();
        Context context = getApplicationContext();

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Toast toast = Toast.makeText(context, "WIFI CONNECTED, UPLOAD STARTING", Toast.LENGTH_SHORT);
            toast.show();

            //PART ONE, WITH THE RELEVANT MACHINE ID CHECK THE SESSIONS IN THE DATABASE TO SEE IF THE SESSION IDs NEED TO BE UPDATED IN APP.
            String url = Links.specificMowerMax + Driver_mainpage.machineID;

        } else {
            Toast toast = Toast.makeText(context, "WIFI NOT CONNECTED, CONNECT AND TRY AGAIN.", Toast.LENGTH_SHORT);
            toast.show();
        }

        if(start != null && start.getIntExtra("Start",0) == 1)
        {
            for (int i = 0; i < 100; i++) {
                new Thread() {
                    @Override()
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.incrementProgressBy(1);
                        }
                    }
                }.start();
            }
        }
    }
    public void JSonVolley(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "got a response");
                //manipulate response
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        try {
                            JSonToArray(jsonObject, url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    //TRY HERE


                    //finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonArrayRequest);
    }

    //Code to transfer retrieved JSON data into arraylists in the application.
    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {
        if (url.equals(Links.allMowerData)) {
            InfoArrays.type_Mower.add(jsonObject.getString(Keys.Type));
            Log.d(TAG, "TYPE INPUT");
            InfoArrays.id_Mower.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.id_workGroup.add(jsonObject.getInt(Keys.id_workGround));
            InfoArrays.name_Mower.add(jsonObject.getString(Keys.name_Mower));
            Log.d(TAG, "Name INPUT");

        } else if (url.equals(Links.allSessionData)) {

            InfoArrays.id_dataSD.add(jsonObject.getInt(Keys.session_data_id));
            InfoArrays.id_MowerSD.add(jsonObject.getString(Keys.session_mower_id));
            InfoArrays.timeSD.add(jsonObject.getString(Keys.session_data_time));
            InfoArrays.GPS_XSD.add(jsonObject.getDouble(Keys.session_data_Gps_x));
            InfoArrays.GPS_YSD.add(jsonObject.getDouble(Keys.session_data_Gps_y));
            InfoArrays.JoystickSD.add(jsonObject.getDouble(Keys.session_data_Joystic));
            InfoArrays.Oil_TempSD.add(jsonObject.getDouble(Keys.session_data_Oil_temp));
            InfoArrays.Acc_X_1SD.add(jsonObject.getDouble(Keys.session_data_Acc_x_1));
            InfoArrays.Acc_Y_1SD.add(jsonObject.getDouble(Keys.session_data_Acc_y_1));
            InfoArrays.Acc_Z_1SD.add(jsonObject.getDouble(Keys.session_data_Acc_z_1));
            InfoArrays.Pitch_1SD.add(jsonObject.getDouble(Keys.session_data_Pitch_1));
            InfoArrays.Yaw_1SD.add(jsonObject.getDouble(Keys.session_data_Yaw_1));
            InfoArrays.Roll_1SD.add(jsonObject.getDouble(Keys.session_data_Roll_1));

        } else {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));
            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.dateS.add(jsonObject.getString(Keys.session_date));
            InfoArrays.startTimeS.add(jsonObject.getString(Keys.session_startTime));
            InfoArrays.Duration.add(jsonObject.getString(Keys.session_Duration));
        }
        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());
    }
    public void clearOldSessData(){
        InfoArrays.id_sess.clear();
        InfoArrays.id_MowerS.clear();
        InfoArrays.dateS.clear();
        InfoArrays.startTimeS.clear();
        InfoArrays.Duration.clear();
    }
    public void accessDatabase(){
        //db = new CurrencyDBHelper(this);

    }


}
