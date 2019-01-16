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
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProductsListAdapter;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerTappedProducts extends BaseActivity {

    private static String TAG = "CustomerTappedProducts";
    private RequestQueue mRequestQueue;

    private ListView allProductsList;
    private Map<String, String> prodIngriVals;

    private RequestHandler mProductIngredientDataFet;
    private TextView emptyProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: if empty put the button to read NFC
        //TODO: find a way to merge this and OrgAllProducts; exact same thing but Request>method and headers

        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_tapped_products, frameLayout);

        mProductIngredientDataFet = new RequestHandler();
        mRequestQueue = Volley.newRequestQueue(this);
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        mProductIngredientDataFet.setUserCredentials(prefs);
        getIngredients(mRequestQueue, "product");
        prodIngriVals = new HashMap<String, String>();
        emptyProd = findViewById(R.id.empty_products);

        allProductsList = findViewById(R.id.listProducts);


        initToolbar();
        toolbar.setTitle("Tapped Products");
        mIsCustomer = true;
    }




    public void getIngredients(RequestQueue mRequestQueue, final String endPoint) {
        String url = mProductIngredientDataFet.HOST_NAME+ endPoint +"/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response != null){
                    Log.e(TAG, "***productList: " + response);
                    setUpAllTappedProducts(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String>  tokenM = new HashMap<String, String>();
                tokenM.put("Authorization", mProductIngredientDataFet.getToken());

                return tokenM;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        mRequestQueue.add(ingredientsRequest);
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
}
