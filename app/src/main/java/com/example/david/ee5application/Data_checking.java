package com.example.david.ee5application;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Keys;
import com.example.david.ee5application.Databases.Links;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;

public class Data_checking extends AppCompatActivity {

    ListView listView;
    String TAG = "David: ";
    ArrayList<String> MachineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_checking);
        listView = (ListView) findViewById(R.id.listView);

        Context context = getApplicationContext();
        String[] ctype = new String[]{"Brussel", "Gent", "Anterwep"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Fetch data from the Mower Table
        JSonVolley(Links.allMowerData);
        try {
            Thread.sleep(2000);
            } catch (InterruptedException e){
        }
        //ListView
        setListView();
        //Spinner
        Spinner spinner = super.findViewById(R.id.Session_Spinner);
        spinner.setAdapter(adapter);
    }
    public void setListView(){
        for(int i =0; i<InfoArrays.name_Mower.size(); i++) {
            Log.d(TAG, "Adding ArrayList Elements");
            MachineList.add(InfoArrays.name_Mower.get(i));
            Log.d(TAG, "Done Adding ArrayList Elements");
        }
        Log.d(TAG, MachineList.size()+" Is the size of machinelist");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MachineList);
        listView.setAdapter(arrayAdapter);

    }
    //Code to send a JSON volley to the DB
    private void JSonVolley(final String url) {
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
        if(url.equals(Links.allMowerData)) {
            InfoArrays.type_Mower.add(jsonObject.getString(Keys.Type));
            Log.d(TAG, "TYPE INPUT");
            InfoArrays.id_Mower.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.id_workGroup.add(jsonObject.getInt(Keys.id_workGround));
            InfoArrays.name_Mower.add(jsonObject.getString(Keys.name_Mower));
            Log.d(TAG, "Name INPUT");

        }else if(url.equals(Links.allSessionData)) {

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

        }else if(url.equals(Links.allSessions)) {

            InfoArrays.id_sess.add(jsonObject.getInt(Keys.id_sess));
            InfoArrays.id_MowerS.add(jsonObject.getInt(Keys.id_Mower));
            InfoArrays.dateS.add(jsonObject.getString(Keys.session_date));
            InfoArrays.startTimeS.add(jsonObject.getString(Keys.session_startTime));
            InfoArrays.Duration.add(jsonObject.getString(Keys.session_Duration));
        }
        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());

    }
}
