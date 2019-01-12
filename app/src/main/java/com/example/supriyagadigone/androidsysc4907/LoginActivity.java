package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.supriyagadigone.androidsysc4907.Customer.CustomerTappedProducts;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProducts;

import java.util.HashMap;

/**
 * Login page for customer and organization
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private RequestQueue mRequestQueue;
    private String mCustOrgBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mCustOrgBtn = getIntent().getStringExtra("BTN_PRESSED");

        mRequestQueue = Volley.newRequestQueue(this);

        loginButtonClicked();

    }

    private void loginButtonClicked() {

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://69.159.27.129:8000/login/";
                        StringRequest loginRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("LoginActivity", "Token" + response);
                                if (response != null) {
                                    Intent intent;
                                    if (mCustOrgBtn.equals("0")) {
                                        SharedPreferences prefs = getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
                                        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();
                                        if (allEntries.entrySet().size() == 0 || allEntries.entrySet() == null) {
                                            intent = new Intent(LoginActivity.this, QuizActivity.class);
                                        } else {
                                            intent = new Intent(LoginActivity.this, CustomerTappedProducts.class);
                                        }
                                    } else {
                                        intent = new Intent(LoginActivity.this, OrgAllProducts.class);
                                    }
                                    startActivity(intent);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Username or password you entered is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> userCredentials = new HashMap<String, String>();
                                userCredentials.put("username", mUsername.getText().toString());
                                userCredentials.put("password", mPassword.getText().toString());
                                return userCredentials;
                            }
                        };

                        mRequestQueue.add(loginRequest);
                    }

                }
        );
    }


}


