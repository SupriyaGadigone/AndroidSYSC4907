package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.ProductIngredientDataFetcher;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrgAllProducts extends BaseActivity {

    private static String TAG = "OrgAllProducts";
    private RequestQueue mRequestQueue;


    private ListView allProductsList;
    private Map<String,String> prodIngriVals;

    private ProductIngredientDataFetcher mProductIngredientDataFet;
    private TextView emptyProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_all_products, frameLayout);

        mProductIngredientDataFet = new ProductIngredientDataFetcher();
        mRequestQueue = Volley.newRequestQueue(this);
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        mProductIngredientDataFet.setUserCredentials(prefs);
        mProductIngredientDataFet.getIngredients(mRequestQueue);
        prodIngriVals = new HashMap<String,String>();
        emptyProd = findViewById(R.id.empty_products);

        setUpAllProducts();

        allProductsList = findViewById(R.id.listProducts);

        allProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrgAllProducts.this, OrgEditNfc.class);
                startActivity(intent);
            }
        });

        initToolbar();
        mIsCustomer = false;
    }

    private void setUpAllProducts() {
        String url = "http://69.159.27.129:8000/productList/";
        StringRequest productListRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "Products list" + response);

                if (response != null) {
                    try {
                        JSONArray jsonData = new JSONArray(response);
                        for (int i = 0; i < jsonData.length(); i++) {
                            JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                            JSONArray ingredientProdObj = new JSONArray(productJsonObj.getString("ingredient"));
                            String ingredientStr = "";
                            for (int j = 0; j < ingredientProdObj.length(); j++) {
                                JSONArray ingridentValues = new JSONArray(mProductIngredientDataFet.getIngriResponse());
                                for (int k = 0; k < ingridentValues.length(); k++) {
                                   JSONObject ingridentVal = new JSONObject(ingridentValues.get(k).toString());
                                    if(ingredientProdObj.getString(j).equals(ingridentVal.getString("ingredient_id"))){
                                        ingredientStr+=ingridentVal.getString("name") + ", ";
                                    }
                                }
                            }
                            prodIngriVals.put(productJsonObj.getString("name"), ingredientStr);
                        }
                        OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(OrgAllProducts.this, prodIngriVals, mIsCustomer);
                        if(prodIngriVals.size() !=0) {
                            emptyProd.setVisibility(View.GONE);
                        }
                        allProductsList.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error);
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> userCredentials = new HashMap<String, String>();
                userCredentials.put("username", mProductIngredientDataFet.getUsername());
                userCredentials.put("password", mProductIngredientDataFet.getPassword());

                return userCredentials;
            }

        };

        mRequestQueue.add(productListRequest);
    }


}
