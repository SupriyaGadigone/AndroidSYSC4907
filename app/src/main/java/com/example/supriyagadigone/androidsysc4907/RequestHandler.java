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

//http://www.zoftino.com/get-&-post-data-using-http-library-volley-in-android
public class RequestHandler extends AppCompatActivity {

    private static String TAG = "RequestHandler";
    public static final String HOST_NAME = "http://69.159.27.129:8000/";

    private String username;
    private String password;
    private String token;

    private String mResponse;
    private String mEndpoint;
    private String mNfcId;

    private RequestQueue mRequestQueue;
    private Map<String,String> prodInfo;

    OnResponseCallback onResponseCallback= null;

    public RequestHandler(RequestQueue requestQueue) {
        this.mRequestQueue = requestQueue;
    }
    public RequestHandler(RequestQueue requestQueue,OnResponseCallback onResponseCallback, SharedPreferences prefs, String endpoint) {
        this.mRequestQueue = requestQueue;
        this.onResponseCallback = onResponseCallback;
        this.mEndpoint = endpoint;
        setUserCredentials(prefs);
        getRequestResponse();
    }

    public RequestHandler(RequestQueue requestQueue,OnResponseCallback onResponseCallback, String endpoint, Map<String,String> prodInfo) {
        this.mRequestQueue = requestQueue;
        this.onResponseCallback = onResponseCallback;
        this.mEndpoint = endpoint;
        this.prodInfo = prodInfo;
        this.token = prodInfo.get("token");
        getRequestResponseWithParams();
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
                if(mEndpoint.equals("product") && data!=null) {
                    data.put("nfc_id", prodInfo.get("nfc_id"));
                }
                if(mEndpoint.equals("newProduct") && data!=null) {
                    data.put("nfc_id", mNfcId);
                    for (Map.Entry<String, String> entry : prodInfo.entrySet())
                    {
                        data.put(entry.getKey(), entry.getValue());
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

    private Response.Listener<String> getPostResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e(TAG, "Response: " + response);

                onResponseCallback.onResponse(mEndpoint,response);
              //  setResponse(response);
//                try {
//                    JSONArray jsonDataResponse = new JSONArray(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

//                if (getEndPoint().equals("ingredientList")) {
//                    Log.e(TAG, "***Ingredients: " + response);
//                    //TODO: Can make everything JsonArray here to avoid a for loop
//                    setResponse(response);
//                }


            }
        };
    }

    private Response.ErrorListener getPostErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //serverResp.setText("Error getting response");
            }
        };
    }

    public void setUserCredentials(SharedPreferences prefs) {

        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().equals("username")) {
                username = entry.getValue().toString();
            }
            if (entry.getKey().equals("password")) {
                password = entry.getValue().toString();
            }
            if (entry.getKey().equals("token")) {
                token = entry.getValue().toString();
            }
        }
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        this.mResponse = response;
    }

    public String getEndpoint() {
        return mEndpoint;
    }

    public void setEndpoint(String endpoint) {
        mEndpoint = endpoint;
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
