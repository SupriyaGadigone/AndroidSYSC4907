package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OrgLandingPage extends AppCompatActivity {

    private Button mWriteNFCButton;
    private Button mEditNFCButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_landing_page_activity);

        mWriteNFCButton = findViewById(R.id.org_write_nfc_button);
        mWriteNFCButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(OrgLandingPage.this, OrgWriteNfc.class);
                startActivity(quizIntent);
            }
        });

        mEditNFCButton = findViewById(R.id.org_edit_nfc_button);
        mEditNFCButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(OrgLandingPage.this, OrgEditNfc.class);
                startActivity(quizIntent);
            }
        });
    }
}
