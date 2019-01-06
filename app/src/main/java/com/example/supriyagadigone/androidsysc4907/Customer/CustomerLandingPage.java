package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class CustomerLandingPage extends BaseActivity {

    private Button mReadNfcButton;
    private Button mGroceryListButton;
    private Button mProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.customer_landing_page_activity);

        getLayoutInflater().inflate(R.layout.customer_landing_page_activity, frameLayout);
        initToolbar();

        mReadNfcButton = findViewById(R.id.customer_read_nfc_button);
        mReadNfcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(CustomerLandingPage.this, CustomerReadNfc.class);
                startActivity(quizIntent);
            }
        });


        mGroceryListButton = findViewById(R.id.customer_list_button);
        mGroceryListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(CustomerLandingPage.this, CustomerGroceryListPage.class);
                startActivity(quizIntent);
            }
        });

        mProfileButton = findViewById(R.id.customer_profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(CustomerLandingPage.this, CustomerProfilePage.class);
                startActivity(quizIntent);
            }
        });
    }
}
