package com.example.supriyagadigone.androidsysc4907;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue MyRequestQueue ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        MyRequestQueue = Volley.newRequestQueue(this);

        LoginButton();

    }

    public void LoginButton(){
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        username.setHintTextColor(Color.LTGRAY);
        password.setHintTextColor(Color.LTGRAY);

        loginButton.setOnClickListener(
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
                                Log.e("LOGIN", "****"+response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();
                                MyData.put("username", "admin");
                                MyData.put("password", "projectPass");
                                return MyData;
                            }
                        };
                        MyRequestQueue.add(MyStringRequest);
                    }




                }
        );
    }





}


