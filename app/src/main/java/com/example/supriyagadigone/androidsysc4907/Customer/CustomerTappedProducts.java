package com.example.supriyagadigone.androidsysc4907.Customer;

import android.os.Bundle;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class CustomerTappedProducts extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_tapped_products, frameLayout);
        initToolbar();

        //TODO: if empty put the button to read NFC
    }
}
