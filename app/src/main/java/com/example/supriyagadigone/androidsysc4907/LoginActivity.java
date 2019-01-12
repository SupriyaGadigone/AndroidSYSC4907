package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private  EditText username;
    private  EditText password;
    private  Button login_button;
    private RequestQueue MyRequestQueue ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        MyRequestQueue = Volley.newRequestQueue(this);

        LoginButton();

        //OAuth2 stuff https://github.com/mario595/android-oauth-linkedin-example/blob/master/src/co/uk/manifesto/linkedinauthexample/MainActivity.java
    }

    public void LoginButton(){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.loginButton);

        username.setHintTextColor(Color.LTGRAY);
        password.setHintTextColor(Color.LTGRAY);

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (username.getText().toString().equals("user") && password.getText().toString().equals("pass")) {
//                            Toast.makeText(LoginActivity.this, "Username and password is correct",
//                                    Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(LoginActivity.this, QuizActivity.class);
//                            startActivity(intent);
//
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Username and password is NOT correct",
//                                    Toast.LENGTH_SHORT).show();
//                        }

                        String url = "http://74.12.191.252:8000/login/";
                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //This code is executed if the server responds, whether or not the response contains data.
                                //The String 'response' contains the server's response.
                                Log.e("LOGIN", "****"+response);
                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //This code is executed if there is an error.
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();
                                MyData.put("username", "admin"); //Add the data you'd like to send to the server.
                                MyData.put("password", "projectPass"); //Add the data you'd like to send to the server.
                                return MyData;
                            }
                        };
                        MyRequestQueue.add(MyStringRequest);
                    }




                }
        );
    }





}


