package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CustomerLandingPage extends AppCompatActivity {

    private Button mReadNfcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_landing_page_activity);

        mReadNfcButton = findViewById(R.id.customer_read_nfc_button);
        mReadNfcButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(CustomerLandingPage.this, CustomerReadNfc.class);
                startActivity(quizIntent);
            }
        });
    }

}
