package com.example.supriyagadigone.androidsysc4907.Organization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class OrgEditProduct extends BaseActivity {

    private static String TAG = "OrgEditProduct";
    private String mNfcId;
    private String mToken;
    private String mProductName;
    private RequestQueue mRequestQueue;
    private EditText mProductNameView;
    private EditText mNfcIdView;
    private EditText mProductIdView;
    private Spinner mTags; //TODO:get list of tags
    private Button mIngredientsButton;
    private Button mSaveButton;
    private List<String> mIngredientsSelected;
    private Map<String, String> ingridentsData;
    private boolean[] isChekced;

//    private EditText mTags;// Tags are restrictions?
//    private EditText mIngridigents;// Ingridients should have the option to select form and add ingriedients


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_edit_nfc_page, frameLayout);
        LinearLayout linearLayout = findViewById(R.id.edit_nfc_layout);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.lightPurple));
        initToolbar();

        mIngredientsSelected = new ArrayList<>();
        //TODO: fix toolbar
        ingridentsData = new HashMap<>();
        mProductName = getIntent().getStringExtra("PROD_NAME");
        toolbar.setTitle("Edit: " + mProductName);

        mIsCustomer = false;

        mRequestQueue = Volley.newRequestQueue(this);

        mNfcId = getIntent().getStringExtra("NFC_ID");
        mToken = getIntent().getStringExtra("token");

        mProductNameView = findViewById(R.id.product_name);
        mProductNameView.setText(mProductName);

        mNfcIdView = findViewById(R.id.nfc_id);
        mProductIdView = findViewById(R.id.product_id);

        //TODO: tags and ingridents
        getProductInfo("ingredientList");
        getProductInfo("product");

        mTags = findViewById(R.id.tags_options);
        final String[] items = new String[]{"1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        mTags.setAdapter(adapter);

        mIngredientsButton = (Button) findViewById(R.id.ingredients_list);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductInfo("newProduct");
            }
        });
    }

    public void getProductInfo(final String endPoint) {
        String url = RequestHandler.HOST_NAME + endPoint + "/";
        StringRequest productsListRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    if (endPoint.equals("product")) {
                        parseProductData(response);
                    }

                    if (endPoint.equals("ingredientList")) {
                        populateIngredientsData(response);
                    }

                    if(endPoint.equals("newProduct")){

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            protected Map<String, String> getParams() {
                Map<String, String> nfcId = new HashMap<String, String>();
                nfcId.put("nfc_id", mNfcId);
                return nfcId;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> tokenM = new HashMap<String, String>();
                tokenM.put("Authorization", mToken);

                return tokenM;
            }
        };
        mRequestQueue.add(productsListRequest);
    }


    public void parseProductData(String response) {
        Log.e(TAG, "PROD: " + response);

        try {
            JSONArray jsonData = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(jsonData.get(0).toString());
            mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj.getString("product_id"));

            JSONArray ingrJsonData = new JSONArray(productJsonObj.getString("ingredient"));
            parseInfo(ingrJsonData);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void populateIngredientsData(String response) {
        Log.e(TAG, "Ingri: " + response);
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                ingridentsData.put(productJsonObj.getString("name"), productJsonObj.getString("ingredient_id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseInfo(JSONArray ingrJsonData) throws JSONException {
        isChekced = new boolean[ingridentsData.size()];
        Arrays.fill(isChekced, Boolean.FALSE);
        Log.e(TAG, "SIZEE: " + isChekced.length);

        for (int i = 0; i < ingridentsData.size(); i++) {
            for (int j = 0; j < ingrJsonData.length(); j++) {
                JSONObject ingriJsonObj = new JSONObject(ingrJsonData.get(j).toString());
                Log.e(TAG, "NAME: " + ingriJsonObj.getString("name"));
                String id = ingridentsData.get(ingriJsonObj.getString("name"));
                Log.e(TAG, "OTHER: " + ingridentsData.keySet().toArray()[i]);
                if (ingriJsonObj.getString("name").equals(ingridentsData.keySet().toArray()[i])) {
                    Log.e(TAG, "i: " + i);
                    isChekced[i] = true;
                }
            }

        }



        final String[] items = ingridentsData.keySet().toArray(new String[ingridentsData.keySet().size()]);

        mIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(OrgEditProduct.this);
                mBuilder.setTitle("Choose Ingredients");
                mBuilder.setMultiChoiceItems(items, isChekced,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                            }

                        });


                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
}
