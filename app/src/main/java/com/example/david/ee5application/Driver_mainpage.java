package com.example.david.ee5application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Driver_mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_mainpage);

        Button Upload = (Button)findViewById(R.id.UploadButton);
        Button ConnectToBluetooth = (Button)findViewById(R.id.buttonBluetooth);


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_upload = new Intent(Driver_mainpage.this,Upload_page.class);
                data_upload.putExtra("Start",1);
                startActivity(data_upload);

            }
        });
        ConnectToBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blueToothPairing = new Intent(Driver_mainpage.this, BluetoothInit.class);
                startActivity(blueToothPairing);

            }
        });


    }
}
