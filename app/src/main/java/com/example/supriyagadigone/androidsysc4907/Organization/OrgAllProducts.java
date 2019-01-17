package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestQueueSingleton;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrgAllProducts extends BaseActivity implements OnResponseCallback {

    private static String TAG = "OrgAllProducts";
    private RequestQueue mRequestQueue;
    private RequestHandler mRequestHandlerm;

    private Map<String, String> productData;

    private TextView emptyProd;
    private ListView allProductsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_all_products, frameLayout);

        mRequestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        mRequestHandlerm = new RequestHandler(mRequestQueue,
                this,
                prefs,
                "productList");


        productData = new HashMap<>();
        emptyProd = findViewById(R.id.empty_products);
        //TODO: once empty, write to NFC option should show up


        allProductsList = findViewById(R.id.listProducts);

        allProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editNFCIntent = new Intent(OrgAllProducts.this, OrgEditProduct.class);
                editNFCIntent.putExtra("PROD_NAME", (String) productData.keySet().toArray()[position]);
                editNFCIntent.putExtra("NFC_ID", productData.get(productData.keySet().toArray()[position]));
                editNFCIntent.putExtra("token", mRequestHandlerm.getToken());
                startActivity(editNFCIntent);
            }
        });

        initToolbar();
        toolbar.setTitle("All Products");

        mIsCustomer = false;
    }


    public void parseProductsData(String response) {
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                productData.put(productJsonObj.getString("name"), productJsonObj.getString("nfc_id"));
            }
            OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(OrgAllProducts.this, productData, mIsCustomer);
            if (productData.size() != 0) {
                emptyProd.setVisibility(View.GONE);
            }
            allProductsList.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResponse(String endpoint, String response) {
        Log.e(TAG, "ALL HERE: " + response);
        parseProductsData(response);

    }
}
