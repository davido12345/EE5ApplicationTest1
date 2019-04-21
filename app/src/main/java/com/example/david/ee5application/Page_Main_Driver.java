package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Page_Main_Driver extends AppCompatActivity {

    //IMPORTANT TO COLLECT ALL OF THESE!
    //*************************************************************************************************************
    public static final int machineID = 2; // SET IN THE CONFIG FILE.
    //*************************************************************************************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_mainpage);

        Button Upload = (Button)findViewById(R.id.UploadButton);
        Button ConnectToBluetooth = (Button)findViewById(R.id.buttonBluetooth);

        Context context = getApplicationContext();
        Database_Session_Storage db = new Database_Session_Storage(context);
        if(db.getEntriesCount() == 24)
        {
            getApplicationContext().deleteDatabase("Session_Data");
        }

        //Button to open the upload class
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_upload = new Intent(Page_Main_Driver.this,Upload_page.class);
                data_upload.putExtra("Start",1);
                startActivity(data_upload);

            }
        });

        //Button to open Bluetooth pairing
        ConnectToBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blueToothPairing = new Intent(Page_Main_Driver.this, Page_Finding_Bluetooth_Device.class);
                startActivity(blueToothPairing);

            }
        });


    }
}
