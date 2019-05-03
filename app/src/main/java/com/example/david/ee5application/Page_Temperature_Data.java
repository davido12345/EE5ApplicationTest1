package com.example.david.ee5application;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.ee5application.Databases.InfoArrays;
import com.example.david.ee5application.Databases.Links;

import java.util.ArrayList;

public class Page_Temperature_Data extends AppCompatActivity {
    ArrayList<Double> Pitches = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_sensor_data_page);
        listView = findViewById(R.id.tempData);
        Pitches = InfoArrays.w_1SD;

        //ListView

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Pitches);
        listView.setAdapter(arrayAdapter);

    }


}
