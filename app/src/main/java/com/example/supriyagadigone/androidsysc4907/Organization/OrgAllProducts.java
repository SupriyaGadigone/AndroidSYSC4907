package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
    private String username;
    private String password;
    private String token;

    private ListView allProductsList;
    private Map<String,String> prodIngriVals;

    private String ingriResponse;
    private TextView emptyProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_all_products, frameLayout);

        mRequestQueue = Volley.newRequestQueue(this);
        prodIngriVals = new HashMap<String,String>();
        emptyProd = findViewById(R.id.empty_products);
        setUserCredentials();
        getIngredients();
        setUpAllProducts();

        allProductsList = findViewById(R.id.listProducts);

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
                                JSONArray ingridentValues = new JSONArray(getIngriResponse());
                                for (int k = 0; k < ingridentValues.length(); k++) {
                                   JSONObject ingridentVal = new JSONObject(ingridentValues.get(k).toString());
                                    if(ingredientProdObj.getString(j).equals(ingridentVal.getString("ingredient_id"))){
                                        ingredientStr+=ingridentVal.getString("name") + ", ";
                                    }
                                }
                            }
                            prodIngriVals.put(productJsonObj.getString("name"), ingredientStr);
                        }
                        OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(OrgAllProducts.this, prodIngriVals);
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
                userCredentials.put("username", username);
                userCredentials.put("password", password);

                return userCredentials;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  tokenMap = new HashMap<String, String>();
                tokenMap.put("Authorization", token);

                return tokenMap;
            }
        };

        mRequestQueue.add(productListRequest);
    }

    private void getIngredients() {
        String url = "http://69.159.27.129:8000/ingredientList/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "***Ingredients: " + response);
                setIngriResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> userCredentials = new HashMap<String, String>();
                userCredentials.put("username", username);
                userCredentials.put("password", password);

                return userCredentials;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  tokenMap = new HashMap<String, String>();
                tokenMap.put("Authorization", token);

                return tokenMap;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        mRequestQueue.add(ingredientsRequest);


    }

    private void setUserCredentials(){
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().equals("username")){
                username = entry.getValue().toString();
            }
            if(entry.getKey().equals("password")){
                password = entry.getValue().toString();
            }
            if(entry.getKey().equals("token")){
                token = entry.getValue().toString();
            }
        }
    }

    public String getIngriResponse() {
        return ingriResponse;
    }

    public void setIngriResponse(String ingriResponse) {
        this.ingriResponse = ingriResponse;
    }
}
