package com.example.supriyagadigone.androidsysc4907;

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

public class RequestHandler extends AppCompatActivity {

    private static String TAG = "RequestHandler";

    private String username;
    private String password;
    private String token;

    public static final String HOST_NAME = "http://69.159.27.129:8000/";

    private String ingriResponse;
    private String prodTappedListResponse;

    public RequestHandler(){

    }


    public void getRequestResponse(RequestQueue mRequestQueue, final String endPoint) {
        String url = HOST_NAME+ endPoint +"/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(endPoint.equals("ingredientList")){
                    Log.e(TAG, "***Ingredients: " + response);
                    setIngriResponse(response);
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
                tokenM.put("Authorization", token);

                return tokenM;
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

    public String getProdTappedListResponse() {
        return prodTappedListResponse;
    }

    public void setProdTappedListResponse(String prodTappedListResponse) {
        this.prodTappedListResponse = prodTappedListResponse;
    }

}
