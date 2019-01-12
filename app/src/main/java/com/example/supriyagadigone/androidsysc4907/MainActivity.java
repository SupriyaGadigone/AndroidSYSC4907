package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgLandingPage;

/**
 * Landing page of the app
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        initToolbar();

        Button mCustomerButton = findViewById(R.id.customer_button);
        mCustomerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(quizIntent);
            }
        });

        // TODO: have keep track of the first click so that a new Activity can open up next time??
        // TODO: have to keep track if they are customers and org
        Button mOrganizationButton = findViewById(R.id.org_button);
        mOrganizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, OrgLandingPage.class);
                startActivity(quizIntent);
            }
        });

    }

}
