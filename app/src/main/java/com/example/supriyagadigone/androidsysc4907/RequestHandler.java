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

//http://www.zoftino.com/get-&-post-data-using-http-library-volley-in-android
public class RequestHandler extends AppCompatActivity {

    private static String TAG = "RequestHandler";
    private static final String HOST_NAME = "http://api.grocer-tap.com/";

    private String token;
    private String mNfcId;

    private RequestQueue mRequestQueue;

    private Map<String, String> prodInfo;
    private String mEndpoint;

    OnResponseCallback onResponseCallback = null;

    public RequestHandler(Context c) {
        this.mRequestQueue = RequestQueueSingleton.getInstance(c).getRequestQueue();
        setUserCredentials(c);
    }

    public RequestHandler(Context c, OnResponseCallback onResponseCallback, String endpoint) {
        this(c);
        this.onResponseCallback = onResponseCallback;
        this.mEndpoint = endpoint;
        getRequestResponse();
    }


    public RequestHandler(Context c, OnResponseCallback onResponseCallback, String endpoint, Map<String, String> prodInfo) {
        this(c);
        //TODO: access the singleton here by calling the default constructor
        this.onResponseCallback = onResponseCallback;
        this.mEndpoint = endpoint;
        this.prodInfo = prodInfo;
        this.mNfcId = prodInfo.get("nfc_id");

        if (endpoint.equals("login")||endpoint.equals("newUser")) {
            getRequestLogin();
        } else{
            getRequestResponseWithParams();
        }
    }

    public void getRequestResponse() {
        String url = HOST_NAME + mEndpoint + "/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.POST, url, getPostResponseListener(), getPostErrorListener()) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> tokenM = new HashMap<String, String>();
                tokenM.put("Authorization", token);

                return tokenM;
            }
        };

        mRequestQueue.add(ingredientsRequest);
    }

    public void getRequestResponseWithParams() {
        String url = HOST_NAME + mEndpoint + "/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.POST, url, getPostResponseListener(), getPostErrorListener()) {

            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<String, String>();
                if (mEndpoint.equals("product") && data != null) {
                    if(prodInfo.get("product_id")!=null){
                        data.put("product_id", prodInfo.get("product_id"));
                    }
                    if(prodInfo.get("nfc_id")!=null){
                        data.put("nfc_id", prodInfo.get("nfc_id"));
                    }
                }

                if (mEndpoint.equals("tag") && data != null) {
                    for (Map.Entry<String, String> entry : prodInfo.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null) {
                            data.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if (mEndpoint.equals("newProduct") && data != null) {
                    if (mNfcId != null) {
                        data.put("nfc_id", mNfcId);
                    }
                    for (Map.Entry<String, String> entry : prodInfo.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null) {
                            data.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                if ((mEndpoint.equals("login") || mEndpoint.equals("shoppingList") || mEndpoint.equals("restrictions"))  && data != null) {
                    for (Map.Entry<String, String> entry : prodInfo.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null) {
                            data.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                Log.e(TAG,"*Endpoint:* " + mEndpoint );

                for (Map.Entry<String, String> entry : data.entrySet()) {
                    Log.e(TAG, mEndpoint);
                    if (entry.getKey() != null && entry.getValue() != null) {
                        Log.e(TAG, entry.getKey() +" *:* "+entry.getValue());
                    }
                }

                return data;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> tokenM = new HashMap<String, String>();
                tokenM.put("Authorization", token);
                return tokenM;
            }


        };

        mRequestQueue.add(ingredientsRequest);
    }

    public void getRequestLogin() {
        String url = HOST_NAME + mEndpoint + "/";
        StringRequest ingredientsRequest = new StringRequest(Request.Method.POST, url, getPostResponseListener(), getPostErrorListener()) {

            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<String, String>();

                for (Map.Entry<String, String> entry : prodInfo.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        data.put(entry.getKey(), entry.getValue());
                    }

                }

                return data;
            }
        };
        mRequestQueue.add(ingredientsRequest);
    }

    private Response.Listener<String> getPostResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Log.e(TAG,"Endpoint: " + mEndpoint );
               // Log.e(TAG,"Response: " + response );
                onResponseCallback.onResponse(mEndpoint, response);
                //TODO: Can make everything JsonArray here to avoid a for loop

            }
        };
    }

    private Response.ErrorListener getPostErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "ERROR: " + error.toString());

            }
        };
    }

    public void setUserCredentials(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().equals("token")) {
                token = entry.getValue().toString();
            }
        }
    }

}
