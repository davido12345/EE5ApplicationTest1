package com.example.david.ee5application;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class LoginPageActivity extends AppCompatActivity {
        String TAG = "Login Page: ";
        String passCheck;
        String password;
        ArrayList<String> validUsers = new ArrayList<String>();
        ArrayList<String> validPasswords = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        final EditText usernameField = (EditText)findViewById(R.id.loginUser);
        final EditText passwordField = (EditText)findViewById(R.id.loginPass);
        Button Login = (Button)findViewById(R.id.loginButton);
        validUsers.add("admin");
        validUsers.add("driver");
        validPasswords.add("admin");
        validPasswords.add("driver");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "the validUsers list says: "+validUsers.get(0));
                Log.d(TAG, "the usernameField says: "+usernameField.getText().toString());
                Log.d(TAG, "the validPassword list says: "+validPasswords.get(0));
                Log.d(TAG, "the passwordField says: "+passwordField.getText().toString());
                if(usernameField.getText().toString().equals(validUsers.get(0)) && passwordField.getText().toString().equals(validPasswords.get(0)))
                {
                    Intent admin = new Intent(LoginPageActivity.this,Administrator_mainpage.class);
                    startActivity(admin);
                }
                else if(usernameField.getText().toString().equals(validUsers.get(1)) && passwordField.getText().toString().equals(validPasswords.get(1)))
                {
                    Intent driver = new Intent(LoginPageActivity.this,Driver_mainpage.class);
                    startActivity(driver);
                }
                else
                {
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, "WRONG PASSWORD OR USERNAME!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


    }

    }





