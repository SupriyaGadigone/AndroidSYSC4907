package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements OnResponseCallback {

    private final String TAG = "Register";
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;

    private String mCustOrgBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);


        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);

        mCustOrgBtn = getIntent().getStringExtra("BTN_PRESSED");

        registerButtonClicked();

    }

    public void onResponse(String endpoint, String response) {
        if (endpoint.equals("newUser")) {
            Intent intent = new Intent(Register.this, LoginActivity.class);
            intent.putExtra("BTN_PRESSED", mCustOrgBtn);
            startActivity(intent);
        }
    }

    private void registerButtonClicked() {
        Button registerButton = findViewById(R.id.registerButton);


        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((mUsername.getText().toString().equals("")) || (mPassword.getText().toString().equals("")) || mEmail.getText().toString().equals("")) {
                            Toast.makeText(Register.this, "Invalid Inputs", Toast.LENGTH_SHORT);

                        } else {
                            Map<String, String> userCredentials = new HashMap<String, String>();
                            userCredentials.put("username", mUsername.getText().toString());
                            userCredentials.put("password", mPassword.getText().toString());
                            userCredentials.put("email", mEmail.getText().toString());

                            RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                                    Register.this,
                                    "newUser", userCredentials);
                        }

                    }
                }
        );


    }
}
