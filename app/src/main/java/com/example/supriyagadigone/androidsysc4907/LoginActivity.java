package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

    private EditText mUsername;
    private EditText mPassword;
    private RequestQueue mRequestQueue;
    private String mCustOrgBtn;
    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mCustOrgBtn = getIntent().getStringExtra("BTN_PRESSED");

        mRequestQueue = Volley.newRequestQueue(this);

        mContext = getApplicationContext();

        loginButtonClicked();

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


    public void onResponse(String endpoint, String response) {
        setUserInfo(response);
    }

    private void setUserInfo(String response) {
        Intent intent;
        if (mCustOrgBtn.equals("0")) {
            SharedPreferences prefs = getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
            Map<String, String> allEntries = (Map<String, String>) prefs.getAll();
            if (allEntries.entrySet().size() == 0 || allEntries.entrySet() == null) {
                intent = new Intent(LoginActivity.this, QuizActivity.class);
            } else {
                // intent = new Intent(LoginActivity.this, CustomerTappedProducts.class);
                intent = new Intent(LoginActivity.this, UserLandingPage.class);
            }
        } else {
            // intent = new Intent(LoginActivity.this, OrgAllProducts.class);
            intent = new Intent(LoginActivity.this, UserLandingPage.class);
        }

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            JSONObject jObject = new JSONObject(response);
            String token = jObject.getString("token");
            editor.putString("token", "token " + token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();

        intent.putExtra("BTN_PRESSED", mCustOrgBtn);
        startActivity(intent);
    }
}


