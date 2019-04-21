package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import static com.example.david.ee5application.Driver_mainpage.machineID;

public class Upload_page extends AppCompatActivity {
    public String TAG = "UPLOAD PAGE:";
    private ProgressBar progressBar;
    public static int maxSessionInDatabase;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_page);

        progressBar = findViewById(R.id.Progress);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        Intent start = getIntent();
        Context context = getApplicationContext();
        mContext = context;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Toast toast = Toast.makeText(context, "WIFI CONNECTED, UPLOAD STARTING", Toast.LENGTH_SHORT);
            toast.show();

            //PART ONE, WITH THE RELEVANT MACHINE ID CHECK THE SESSIONS IN THE DATABASE TO SEE IF THE SESSION IDs NEED TO BE UPDATED IN APP.

            App_Database db = new App_Database(context);
            int maximumTableStored = db.checkMax();
            JSonVolley(Links.specificMowerMax+machineID);
            //SOME KIND OF LOOP:



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

        if (url.equals(Links.specificMowerMax+machineID)) {
            InfoArrays.maxSession = jsonObject.getInt(Keys.maximumSessionValue);
            Log.d(TAG, "Received from DB MaxSession = "+jsonObject.getInt(Keys.maximumSessionValue));
            maxSessionInDatabase = jsonObject.getInt(Keys.maximumSessionValue);


            //UPDATE ALL THE SESSION_ID VALUES
            //Then pushes all data into the database
            App_Database db = new App_Database(mContext);
            long dbEntries = db.getEntriesCount();
            Log.d(TAG,"dbEntries are equal to: "+dbEntries);
            //For a newly initialized database

            for(int i = 0;  i<dbEntries; i++)
            {
                //WORKS TO FETCH RECORDS!!!
                SessionDataDetails item = (SessionDataDetails) db.getAllSessionData().get(i);
                int sessionId = item.getSession_id();
                int packet_Id = item.getPacket_id();
                item.setSession_id(sessionId+maxSessionInDatabase);
                db.deletePacket(packet_Id, sessionId);
                db.addNewPacket(packet_Id , machineID, item.getKey_Date(), item.getKey_Time(), item.getKey_Gps_x(), item.getKey_Gps_y(), item.getKey_Joystick(),
                    item.getKey_Oil_Temp(), item.getKey_Pitch_1(), item.getKey_Roll_1(), item.getKey_Yaw_1(), item.getKey_Pitch_2(),
                    item.getKey_Roll_2(), item.getKey_Yaw_2(), item.getKey_Pitch_3(), item.getKey_Roll_3(), item.getKey_Yaw_3()
    );
                Log.d(TAG, "USING LISTS COMPONENT OF GPS_X: "+item.getKey_Gps_x());
                Log.d(TAG, "SESSION ID!: "+item.getSession_id());

                Log.d(TAG, "UPLOADING A DATA!");
                String insertPacketToDataBase = "https://a18ee5mow2.studev.groept.be/InsertSessionData.php?id_Session="+item.getSession_id()+"&id_Mower="+Driver_mainpage.machineID+
                        "&time_SessionData="+item.getKey_Time()+"&Gps_x="+item.getKey_Gps_x()+"&Gps_y="+item.getKey_Gps_x()+"&Joystick="+item.getKey_Joystick()+
                        "&Oil_temp="+item.getKey_Oil_Temp()+"&Pitch_1="+item.getKey_Pitch_1()+"&Roll_1="+item.getKey_Roll_1()+
                        "&Yaw_1="+item.getKey_Yaw_1()+"&Pitch_2="+item.getKey_Pitch_2()+"&Roll_2="+item.getKey_Roll_2()+"&Yaw_2="+item.getKey_Yaw_2()+
                        "&Pitch_3="+item.getKey_Pitch_3()+"&Roll_3="+item.getKey_Roll_3()+"&Yaw_3="+item.getKey_Yaw_3()+"";

                JSonVolley(insertPacketToDataBase);

            }
            db.close();

            //WORKING INSERT BUT INSERTS BLANK!
            //DEACTIVATE TO NOT FILL DATABASE WITH JUNK
            /*
            for(int i=0; i<db.getEntriesCount(); i++)
            {
                Log.d(TAG, "UPLOADING A DATA!");
                SessionDataDetails selection = db.getSessionData(i);
                String insertPacketToDataBase = "https://a18ee5mow2.studev.groept.be/InsertSessionData.php?" +
                        "id_SessionData="+selection.getSession_id()+"&id_Session="+selection.getSession_id()+"&id_Mower="+selection.getKey_Mower_id()+
                        "&time_SessionData="+selection.getKey_Time()+"&Gps_x="+selection.getKey_Gps_x()+"&Gps_y="+selection.getKey_Gps_x()+"&Joystick="+selection.getKey_Joystick()+
                        "&Oil_temp="+selection.getKey_Oil_Temp()+"&Pitch_1="+selection.getKey_Pitch_1()+"&Roll_1="+selection.getKey_Roll_1()+
                        "&Yaw_1="+selection.getKey_Yaw_1()+"&Pitch_2="+selection.getKey_Pitch_2()+"&Roll_2="+selection.getKey_Roll_2()+"&Yaw_2="+selection.getKey_Yaw_2()+
                        "&Pitch_3="+selection.getKey_Pitch_3()+"&Roll_3="+selection.getKey_Roll_3()+"&Yaw_3="+selection.getKey_Yaw_3()+"";

                JSonVolley(insertPacketToDataBase);

                }*/
            }


        }

        //Log.d(TAG, "getting size :" + InfoArrays.firstNames.size());

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
    public void adjustIndexes(){

    }

}
