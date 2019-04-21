package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class RecordingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button endActivity = (Button)findViewById(R.id.buttonEndSession);

        endActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BluetoothConnectionService.end = true;
                Intent data_check = new Intent(RecordingActivity.this,  Driver_mainpage.class);
                startActivity(data_check);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Don't Forget to upload the session when you have wifi!!!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }

}
