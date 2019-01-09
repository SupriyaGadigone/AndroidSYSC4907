package com.example.supriyagadigone.androidsysc4907;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;

public class LoginActivity extends AppCompatActivity {
    private static EditText username;
    private static EditText password;
    private static Button login_button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        LoginButton();

        //OAuth2 stuff https://github.com/mario595/android-oauth-linkedin-example/blob/master/src/co/uk/manifesto/linkedinauthexample/MainActivity.java
    }

    public void LoginButton(){
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login_button = (Button)findViewById(R.id.loginButton);

        username.setHintTextColor(Color.LTGRAY);
        password.setHintTextColor(Color.LTGRAY);

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("user") && password.getText().toString().equals("pass")) {
                            Toast.makeText(LoginActivity.this, "Username and password is correct",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, QuizActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Username and password is NOT correct",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}


