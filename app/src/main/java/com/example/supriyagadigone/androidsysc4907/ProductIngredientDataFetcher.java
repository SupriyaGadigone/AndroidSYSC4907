package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProductIngredientDataFetcher extends AppCompatActivity {

    private static String TAG = "ProductIngredientDataFetcher";

    private String username;
    private String password;
    private String token;

    private String ingriResponse;


    public void getIngredients(RequestQueue mRequestQueue) {
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
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        mRequestQueue.add(ingredientsRequest);
    }

    public void setUserCredentials(SharedPreferences prefs){

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
