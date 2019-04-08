package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.david.ee5application.Data_checking_M_Name_List.machineSelected;

public class Arm_state_page extends AppCompatActivity {
    int machineID = machineSelected;
    String queryURL = Links.specificSessionsGPS+machineID;
    String TAG = "ArmStatePage: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arm_state_page);

        Button mapCheck = (Button)findViewById(R.id.mapButtonArm);

        mapCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Preparing Map", Toast.LENGTH_SHORT);
                toast.show();
                JSonVolley(queryURL);


            }
        });
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

                    //TRY NEW ACTIVITY LAUNCH HERE
                    Intent Map = new Intent(Arm_state_page.this,Maps.class);
                    startActivity(Map);
                    finish();

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

    public void JSonToArray (JSONObject jsonObject, String url) throws Exception {
        if (url.equals(Links.allMowerData)) {

        } else if (url.equals(Links.allSessionData)) {

        } else if (url.equals(Links.allSessions)) {

        } else if (url.equals(Links.specificSessionsGPS+machineID)) {

            LatLng temp = new LatLng(jsonObject.getDouble(Keys.session_data_Gps_x), jsonObject.getDouble(Keys.session_data_Gps_y));
            InfoArrays.GpsLocations.add(temp);
            InfoArrays.GpsLocationsX.add(jsonObject.getDouble(Keys.session_data_Gps_x));
            InfoArrays.GpsLocationsY.add(jsonObject.getDouble(Keys.session_data_Gps_y));

        }
        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());
    }
}
