package com.example.david.ee5application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Administrator_mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_page);

        Button Data_checking = (Button)findViewById(R.id.Data_check);
        Button Map_checking = (Button)findViewById(R.id.Map_check);

        Data_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data_check = new Intent(Administrator_mainpage.this,Data_checking.class);
                startActivity(data_check);
            }
        });

        Map_checking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map_check = new Intent(Administrator_mainpage.this,Maps.class);
                startActivity(map_check);
            }
        });


    }
}
