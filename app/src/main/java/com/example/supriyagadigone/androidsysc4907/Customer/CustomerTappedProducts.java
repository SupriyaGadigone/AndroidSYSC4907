package com.example.supriyagadigone.androidsysc4907.Customer;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProductsListAdapter;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerTappedProducts extends BaseActivity implements OnResponseCallback {

    private static String TAG = "CustomerTappedProducts";

    private ListView allProductsList;
    private Map<String, String> prodIngriVals;

    private TextView emptyProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: pass the proper data FIX

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_tapped_products, frameLayout);

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "product");

        prodIngriVals = new HashMap<>();
        emptyProd = findViewById(R.id.empty_products);

        initToolbar();
        toolbar.setTitle("Tapped Products");

        allProductsList = findViewById(R.id.listProducts);

        for(String m : prodIngriVals.keySet()){
            Log.e(TAG, "vals: "+m);
        }
        allProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent tappedNFCIntent = new Intent(CustomerTappedProducts.this, CustomerTappedProductInfo.class);
                Log.e(TAG, "Position: "+position);
                Log.e(TAG, "Size: " +prodIngriVals.size());
                tappedNFCIntent.putExtra("PROD_DATA", prodIngriVals.get(prodIngriVals.keySet().toArray()[position]));
                startActivity(tappedNFCIntent);
            }
        });


        mIsCustomer = true;
    }


    private void setUpAllTappedProducts(String response) {
        Log.e(TAG, "TAPPED resp: "+ response);
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                prodIngriVals.put(productJsonObj.getString("name"), response);
            }
            OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(CustomerTappedProducts.this, prodIngriVals, mIsCustomer);
            if (prodIngriVals.size() == 0) {
                emptyProd.setVisibility(View.VISIBLE);
            }
            allProductsList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onResponse(String endpoint,String response) {
        setUpAllTappedProducts(response);
    }
}
