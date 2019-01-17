package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProductsListAdapter;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerTappedProducts extends BaseActivity implements OnResponseCallback {

    private static String TAG = "CustomerTappedProducts";
    private RequestQueue mRequestQueue;

    private ListView allProductsList;
    private Map<String, String> prodIngriVals;

    private TextView emptyProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: if empty put the button to read NFC
        //TODO: find a way to merge this and OrgAllProducts; exact same thing but Request>method and headers

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_tapped_products, frameLayout);

        mRequestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        RequestHandler mRequestHandlerm = new RequestHandler(mRequestQueue,
                this,
                prefs,
                "product");

        prodIngriVals = new HashMap<>();
        emptyProd = findViewById(R.id.empty_products);

        allProductsList = findViewById(R.id.listProducts);

        initToolbar();
        toolbar.setTitle("Tapped Products");
        mIsCustomer = true;
    }


    private void setUpAllTappedProducts(String response) {
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                prodIngriVals.put(productJsonObj.getString("name"), "");
            }
            OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(CustomerTappedProducts.this, prodIngriVals, mIsCustomer);
            if (prodIngriVals.size() != 0) {
                emptyProd.setVisibility(View.GONE);
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
