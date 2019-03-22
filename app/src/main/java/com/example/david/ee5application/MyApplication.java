package com.example.david.ee5application;

import android.app.Application;
import android.content.Context;
//THIS SHOULD STILL BE FIXED
//This is used in Loading Page 1 to inform user that the loading is complete in the JSON to array method.
//Currently it accomplishes nothing!
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}