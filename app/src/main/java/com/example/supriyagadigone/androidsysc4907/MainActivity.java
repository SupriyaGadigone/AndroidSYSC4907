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

public class MainActivity extends BaseActivity {

    private Button mCustomerButton;
    private Button mOrganizationButton;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        initToolbar();

        mCustomerButton = findViewById(R.id.customer_button);
        mCustomerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(quizIntent);
            }
        });

        // TODO: have keep track of the first click so that a new Activity can open up next time??
        // TODO: have to keep track if they are customers and org
        mOrganizationButton = findViewById(R.id.org_button);
        mOrganizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(MainActivity.this, OrgLandingPage.class);
                startActivity(quizIntent);
            }
        });

//        dl = (DrawerLayout)findViewById(R.id.navigation_drawer);
//        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
//
//        dl.addDrawerListener(t);
//        t.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        nv = (NavigationView)findViewById(R.id.nv);
//        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                switch(id)
//                {
//                    case R.id.account:
//                        Toast.makeText(MainActivity.this, "My Account",Toast.LENGTH_SHORT).show();
//                    case R.id.settings:
//                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
//                    case R.id.mycart:
//                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
//                    default:
//                        return true;
//                }
//
//
//
//
//            }
//        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if(t.onOptionsItemSelected(item))
//            return true;
//
//        return super.onOptionsItemSelected(item);
//    }
}
