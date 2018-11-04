package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mCustomerButton;
    private Button mOrganizationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomerButton = findViewById(R.id.customer_button);
        mCustomerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(quizIntent);
            }
        });

        // have keep track of the first click so that a new Activity can open up next time
        // have to keep track if they are customers and org as well
        mOrganizationButton = findViewById(R.id.org_button);
        mOrganizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, OrgLandingPage.class);
                startActivity(quizIntent);
            }
        });
    }
}
