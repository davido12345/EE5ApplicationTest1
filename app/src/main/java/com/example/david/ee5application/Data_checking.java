package com.example.david.ee5application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Data_checking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_checking);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.choose, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = super.findViewById(R.id.Session_Spinner);
        Button OK = (Button) findViewById(R.id.OK_button);

        spinner.setAdapter(adapter);
        OK.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        intent.setClass(Data_checking.this, Data_page.class);
                        startActivity(intent);
                        break;
                }
            }
        });

    }
}
