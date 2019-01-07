package com.example.supriyagadigone.androidsysc4907.Customer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

public class CustomerGroceryListPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_grocery_list_page, frameLayout);
        initToolbar();
    }
}
