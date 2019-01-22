package com.example.supriyagadigone.androidsysc4907.Organization;

import android.os.Bundle;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class OrgProfilePage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_profile_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Profile");
        mIsCustomer = false;
    }
}
