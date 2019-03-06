package com.example.david.ee5application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Upload_page extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_page);

        progressBar = findViewById(R.id.Progress);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        Intent start = getIntent();
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


}
