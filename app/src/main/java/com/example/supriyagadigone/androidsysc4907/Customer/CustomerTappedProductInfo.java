package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.ParseProductInfo;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomerTappedProductInfo extends BaseActivity {

    private static String TAG = "CustomerTappedProductsInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View v = getLayoutInflater().inflate(R.layout.customer_tapped_products_info, frameLayout);

        initToolbar();
        toolbar.setTitle("Tapped Product");

        ParseProductInfo p = new ParseProductInfo();
        p.parseProductData("{\"product\":"+getIntent().getStringExtra("PROD_DATA")+"}", v, this);

        mIsCustomer = true;
    }



}
