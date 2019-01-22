package com.example.supriyagadigone.androidsysc4907.Organization;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.UserLandingPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OrgEditProduct extends BaseActivity implements OnResponseCallback {

    private static String TAG = "OrgEditProduct";
    private String mNfcId;
    private String mProductData;
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

        mIsCustomer = false;
        prodInfo = new HashMap<>();

        mNfcId = getIntent().getStringExtra("NFC_ID");

        //TODO: fix toolbar
        initToolbar();
        ingridentsData = new HashMap<>();

        mProductData = getIntent().getStringExtra("PROD_DATA");
        Log.e(TAG, "EDIT PROD: " + mProductData);

        mProductNameView = findViewById(R.id.product_name);

        try {
            //JSONArray productJsonArr = new JSONArray(mProductData);
            JSONObject productJsonObj = new JSONObject(mProductData);
            prodInfo.put("nfc_id", productJsonObj.getString("nfc_id"));
            toolbar.setTitle("Edit: " + productJsonObj.getString("name"));
            mProductNameView.setText(productJsonObj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestHandler mRequestHandlerm1 = new RequestHandler(getApplicationContext(),
                this,
                "ingredientList", prodInfo);
        RequestHandler mRequestHandlerm2 = new RequestHandler(getApplicationContext(),
                this,
                "product", prodInfo);




        mNfcIdView = findViewById(R.id.nfc_id);
        mProductIdView = findViewById(R.id.product_id);

        //TODO: tags and ingridents


        mTags = findViewById(R.id.tags_options);

        mBuilder = new AlertDialog.Builder(OrgEditProduct.this);
        ;

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

        try {
            JSONObject productJsonObj = new JSONObject(response);
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

        for (int i = 0; i < ingridentsData.size(); i++) {
            for (int j = 0; j < ingrJsonData.length(); j++) {
                JSONObject ingriJsonObj = new JSONObject(ingrJsonData.get(j).toString());
                String id = ingridentsData.get(ingriJsonObj.getString("name"));
                if (ingriJsonObj.getString("name").equals(ingridentsData.keySet().toArray()[i])) {
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


    }

    private void editProductInfo() {
        prodInfo.put("new_name", mProductNameView.getText().toString());
        prodInfo.put("new_nfc_id", mNfcIdView.getText().toString());
        prodInfo.put("new_product_id", mProductIdView.getText().toString());
        prodInfo.put("new_tags", mTags.getItemAtPosition(0).toString());
        String s = "";
        for (int i = 0; i <= mSelectedItems.size() - 1; i++) {

            s += mSelectedItems.get(i) + ",";
        }
        prodInfo.put("new_ingredientId", s);
        RequestHandler mRequestHandlerm3 = new RequestHandler(getApplicationContext(),
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
            Intent allProd = new Intent(OrgEditProduct.this, UserLandingPage.class);
            allProd.putExtra("BTN_PRESSED", "1");
            startActivity(allProd);
        }
    }
}
