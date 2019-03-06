package com.example.david.ee5application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Data_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_page);

        Button Temp = (Button)findViewById(R.id.T_Sensor_log);
        Button Ventilator = (Button)findViewById(R.id.Cooling_log);

        Temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(Data_page.this,Temperature_sensor_data_page.class);
                startActivity(admin);
            }
        });

        Ventilator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admin = new Intent(Data_page.this,Ventilator_data_page.class);
                startActivity(admin);
            }
        });


    }
}
