package com.example.supriyagadigone.androidsysc4907.Organization;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class OrgEditNfc extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_edit_nfc_page, frameLayout);
        initToolbar();
        mIsCustomer = false;

    }
}
