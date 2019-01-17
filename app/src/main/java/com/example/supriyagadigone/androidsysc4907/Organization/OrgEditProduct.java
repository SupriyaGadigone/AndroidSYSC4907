package com.example.supriyagadigone.androidsysc4907.Organization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgEditProduct extends BaseActivity implements OnResponseCallback {

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
    private Map<String, String> ingridentsData;
    private String[] tagItems;
    private boolean[] isChecked;
    private Map<String, String> prodInfo;
    private AlertDialog.Builder mBuilder;
    private ArrayList<Integer> mSelectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_edit_nfc_page, frameLayout);
        LinearLayout linearLayout = findViewById(R.id.edit_nfc_layout);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.lightPurple));
        initToolbar();

        //TODO: fix toolbar
        ingridentsData = new HashMap<>();
        mProductName = getIntent().getStringExtra("PROD_NAME");
        toolbar.setTitle("Edit: " + mProductName);

        mIsCustomer = false;
        prodInfo = new HashMap<>();

        mRequestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        mNfcId = getIntent().getStringExtra("NFC_ID");
        mToken = getIntent().getStringExtra("token");


        prodInfo.put("nfc_id",mNfcId);
        prodInfo.put("token",mToken);
        RequestHandler mRequestHandlerm1 = new RequestHandler(mRequestQueue,
                this,
                "ingredientList", prodInfo);
        RequestHandler mRequestHandlerm2 = new RequestHandler(mRequestQueue,
                this,
                "product", prodInfo);

        mProductNameView = findViewById(R.id.product_name);
        mProductNameView.setText(mProductName);

        mNfcIdView = findViewById(R.id.nfc_id);
        mProductIdView = findViewById(R.id.product_id);

        //TODO: tags and ingridents


        mTags = findViewById(R.id.tags_options);

         mBuilder = new AlertDialog.Builder(OrgEditProduct.this);;

        mIngredientsButton = (Button) findViewById(R.id.ingredients_list);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProductInfo();
            }
        });
    }


    public void parseProductData(String response) {
        Log.e(TAG, "PROD: " + response);



        try {
            JSONArray jsonData = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(jsonData.get(0).toString());
            mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj.getString("product_id"));
            tagItems = new String[]{productJsonObj.getString("tags")};

            JSONArray ingrJsonData = new JSONArray(productJsonObj.getString("ingredient"));
            parseInfo(ingrJsonData);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);

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
        isChecked = new boolean[ingridentsData.size()];
        mSelectedItems = new ArrayList<>();
        Arrays.fill(isChecked, Boolean.FALSE);
        //Log.e(TAG, "SIZEE: " + isChecked.length);

        for (int i = 0; i < ingridentsData.size(); i++) {
            for (int j = 0; j < ingrJsonData.length(); j++) {
                JSONObject ingriJsonObj = new JSONObject(ingrJsonData.get(j).toString());
                Log.e(TAG, "NAME: " + ingriJsonObj.getString("name"));
                String id = ingridentsData.get(ingriJsonObj.getString("name"));
                Log.e(TAG, "SIZE1: " + mSelectedItems.size());
                Log.e(TAG, "OTHER: " + ingridentsData.keySet().toArray()[i]);
                if (ingriJsonObj.getString("name").equals(ingridentsData.keySet().toArray()[i])) {
                    Log.e(TAG, "i: " + i);
                    mSelectedItems.add(Integer.parseInt(id));
                    isChecked[i] = true;

                }
            }

        }

        setIngredientsButton();

    }

    private void setIngredientsButton() {
        final String[] items = ingridentsData.keySet().toArray(new String[ingridentsData.keySet().size()]);

        mIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "SIZE2: " + mSelectedItems.size());
                mBuilder.setTitle("Choose Ingredients");
                mBuilder
                        .setMultiChoiceItems(items, isChecked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(Integer.parseInt(ingridentsData.get(ingridentsData.keySet().toArray()[i])));
                                } else if (mSelectedItems.contains(i)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(i));
                                }
                            }

                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                ;

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


//        for(int i = 0; i<=mSelectedItems.size() ; i++){
//            Log.e(TAG, "SELECTED: " + mSelectedItems.get(i));
//        }
    }

    private void editProductInfo(){
        prodInfo.put("new_name", mProductNameView.getText().toString());
        prodInfo.put("new_nfc_id", mNfcIdView.getText().toString());
        prodInfo.put("new_product_id", mProductIdView.getText().toString());
        prodInfo.put("new_tags", mTags.getItemAtPosition(0).toString());
        Log.e(TAG, "SIZE3: " + mSelectedItems.size());
        String s = "";
        for(int i = 0; i<=mSelectedItems.size()-1 ; i++){
            Log.e(TAG, "SELECTED: " + mSelectedItems.get(i));
            Log.e(TAG, "i: " + i);
            s+= mSelectedItems.get(i) + ",";
        }
        prodInfo.put("new_ingredientId", s);
        RequestHandler mRequestHandlerm3 = new RequestHandler(mRequestQueue,
                this,
                "newProduct", prodInfo);
    }

    public void onResponse(String endpoint, String response) {
        if (endpoint.equals("product")) {
            parseProductData(response);
        }

        if (endpoint.equals("ingredientList")) {
            populateIngredientsData(response);
        }

        if (endpoint.equals("newProduct")) {
           // editProductInfo();
            Log.e(TAG, "HEHEHEHEHEHHEHE");
        }
    }
}
