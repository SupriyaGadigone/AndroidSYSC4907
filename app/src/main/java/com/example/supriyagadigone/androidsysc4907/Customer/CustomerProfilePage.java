package com.example.supriyagadigone.androidsysc4907.Customer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.RestrictionsData;
import com.example.supriyagadigone.androidsysc4907.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class CustomerProfilePage extends BaseActivity {

    private ListView restrictionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_profile_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Profile");
        mIsCustomer = true;

        List<RestrictionsData> restDataList = Arrays.asList(RestrictionsData.values());

        restrictionsList = findViewById(R.id.restrictions_list);
        // Convert ArrayList to array

        restrictionsList.setAdapter(new ArrayAdapter<RestrictionsData>(CustomerProfilePage.this,
                android.R.layout.simple_list_item_1, restDataList));

        //TODO: add edit quiz
    }
}
