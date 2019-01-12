package com.example.supriyagadigone.androidsysc4907.Organization;

import android.os.Bundle;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class OrgAllProducts extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_all_products, frameLayout);
        initToolbar();
        mIsCustomer = false;

        //TODO: toolbar should have write, read and edit NFC
    }
}
