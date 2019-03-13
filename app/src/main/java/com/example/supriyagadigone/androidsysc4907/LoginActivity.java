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

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Login page for customer and organization
 */
public class LoginActivity extends AppCompatActivity implements OnResponseCallback {

    public static final String LOGIN_PREFS_NAME = "PREFS_CREDENTIALS";
    private final String TAG = "LoginActivity";

    private EditText mUsername;
    private EditText mPassword;
    private String mCustOrgBtn;
    private Context mContext;
    private String mToken;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mCustOrgBtn = getIntent().getStringExtra("BTN_PRESSED");

        mContext = getApplicationContext();

        loginButtonClicked();
        registerButtonClicked();


    }

    private void loginButtonClicked() {

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> userCredentials = new HashMap<String, String>();
                        userCredentials.put("username", mUsername.getText().toString());
                        userCredentials.put("password", mPassword.getText().toString());

                        RequestHandler mRequestHandlerm = new RequestHandler(mContext,
                                LoginActivity.this,
                                "login", userCredentials);

                    }
                }
        );
    }

    private void registerButtonClicked(){
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, Register.class);
                        intent.putExtra("BTN_PRESSED", mCustOrgBtn);
                        startActivity(intent);
                    }
                }
        );

    }


    public void onResponse(String endpoint, String response) {

        if(endpoint.equals("login")) {
            setUserInfo(response);
        }

        if(endpoint.equals("restrictions")) {
            setUserActivity(response);
        }

    }

    private void setUserInfo(String response) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            JSONObject jObject = new JSONObject(response);
            mToken = jObject.getString("token");
            editor.putString("token", "token " + mToken);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();

        RequestHandler mRequestHandlerm = new RequestHandler(mContext,
                LoginActivity.this,
                "restrictions");

    }

    private void setUserActivity(String response){
        Log.e(TAG, response);
        Intent intent;
        if (mCustOrgBtn.equals("0")) {
            if (response.equals("[]")) {
                intent = new Intent(LoginActivity.this, QuizActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, UserLandingPage.class);
            }
        } else {
            intent = new Intent(LoginActivity.this, UserLandingPage.class);
        }

        intent.putExtra("BTN_PRESSED", mCustOrgBtn);
        startActivity(intent);
    }
}


