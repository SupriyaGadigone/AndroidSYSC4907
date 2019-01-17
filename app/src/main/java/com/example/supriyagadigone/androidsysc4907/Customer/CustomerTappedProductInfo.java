package com.example.supriyagadigone.androidsysc4907.Customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomerTappedProductInfo extends BaseActivity {

    private static String TAG = "CustomerTappedProductsInfo";
    private String prodData;

    private TextView mProductNameView;
    private TextView mNfcIdView;
    private TextView mProductIdView;
    private Spinner mTags; //TODO:get list of tags
  //  private Button mIngredientsButton;
    private String[] tagItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_tapped_products_info, frameLayout);

        initToolbar();

        prodData = getIntent().getStringExtra("PROD_DATA");
        Log.e(TAG, "data: "+ prodData);
        toolbar.setTitle("Tapped Product");

        mProductNameView = findViewById(R.id.product_name);


        mNfcIdView = findViewById(R.id.nfc_id);
        mProductIdView = findViewById(R.id.product_id);

        mTags = findViewById(R.id.tags_options);

        parseProductData(prodData);

        mIsCustomer = true;
    }


    public void parseProductData(String response) {
        Log.e(TAG, "PROD: " + response);



        try {
            JSONArray jsonData = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(jsonData.get(0).toString());
            mProductNameView.setText(productJsonObj.getString("name"));
            mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj.getString("product_id"));
            tagItems = new String[]{productJsonObj.getString("tags")};

//            JSONArray ingrJsonData = new JSONArray(productJsonObj.getString("ingredient"));
//            parseInfo(ingrJsonData);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
