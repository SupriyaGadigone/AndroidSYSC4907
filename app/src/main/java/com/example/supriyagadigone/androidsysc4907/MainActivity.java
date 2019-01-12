package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Landing page of the app which consist of the Organization and Customer button
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button customerButton = findViewById(R.id.customer_button);
        customerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent customerIntent = new Intent(MainActivity.this, LoginActivity.class);
                customerIntent.putExtra("BTN_PRESSED", "0"); //0 = the user pressed customer button
                startActivity(customerIntent);
            }
        });

        Button organizationButton = findViewById(R.id.org_button);
        organizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent orgIntent = new Intent(MainActivity.this, LoginActivity.class);
                orgIntent.putExtra("BTN_PRESSED", "1"); //1 = the user pressed organization button
                startActivity(orgIntent);
            }
        });

    }

}
