package com.example.supriyagadigone.androidsysc4907.Organization;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.UserLandingPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrgWriteEditProduct extends BaseActivity implements OnResponseCallback {

    private static String TAG = "OrgWriteEditProduct";
    private String mNfcId;
    private String mIsWrite;
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
    private AlertDialog.Builder mIngridientsBuilder;
    private ArrayList<Integer> mSelectedItems;

    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private String mNewNfcID;
    boolean mWriteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_edit_nfc_page, frameLayout);

        mIsCustomer = false;
        prodInfo = new HashMap<>();

        mNfcId = getIntent().getStringExtra("NFC_ID");
        mIsWrite = getIntent().getStringExtra("IS_WRITE");

        //TODO: fix toolbar
        initToolbar();
        ingridentsData = new HashMap<>();

        //TODO: tags and ingridents
        mProductNameView = findViewById(R.id.product_name);
        mProductIdView = findViewById(R.id.product_id);
        mTags = findViewById(R.id.tags_options);
        mIngredientsButton = findViewById(R.id.ingredients_list);
        mNfcIdView = findViewById(R.id.nfc_id);
        mSaveButton = findViewById(R.id.save_button);
        mIngridientsBuilder = new AlertDialog.Builder(OrgWriteEditProduct.this);



        if (mIsWrite.equals("1")) {
            mProductData = getIntent().getStringExtra("PROD_DATA");

            try {
                //JSONArray productJsonArr = new JSONArray(mProductData);
                JSONObject productJsonObj = new JSONObject(mProductData);
                prodInfo.put("nfc_id", productJsonObj.getString("nfc_id"));
                toolbar.setTitle("Edit: " + productJsonObj.getString("name"));
                mProductNameView.setText(productJsonObj.getString("name"));

                RequestHandler mRequestHandlerm1 = new RequestHandler(getApplicationContext(),
                        this,
                        "ingredientList", prodInfo);
                RequestHandler mRequestHandlerm2 = new RequestHandler(getApplicationContext(),
                        this,
                        "product", prodInfo);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            //Todo: add list of ingridients and tags
            mNfcIdView.setVisibility(View.GONE);
            mSaveButton.setText("Write to NFC");
            findViewById(R.id.nfc_id_tv).setVisibility(View.GONE);
            RequestHandler mRequestHandlerm3 = new RequestHandler(getApplicationContext(),
                    this,
                    "newNFCId");
        }



        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProductInfo();
                if (mIsWrite.equals("0")) {

                    mNfcAdapter = NfcAdapter.getDefaultAdapter(OrgWriteEditProduct.this);
                    mNfcPendingIntent = PendingIntent.getActivity(OrgWriteEditProduct.this, 0,
                            new Intent(OrgWriteEditProduct.this, OrgWriteEditProduct.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                    enableTagWriteMode();

                    new AlertDialog.Builder(OrgWriteEditProduct.this).setTitle("Touch tag to write")
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    disableTagWriteMode();
                                }

                            }).create().show();

                }
            }
        });
    }


    public void parseProductData(String response) {

        try {
            JSONObject productJsonObj = new JSONObject(response);

            if (mIsWrite.equals("1")) {
                mNfcIdView.setText(productJsonObj.getString("nfc_id"));
                mProductIdView.setText(productJsonObj.getString("product_id"));
            }
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
                mIngridientsBuilder.setTitle("Choose Ingredients");
                mIngridientsBuilder
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

                AlertDialog mDialog = mIngridientsBuilder.create();
                mDialog.show();
            }
        });


    }

    private void editProductInfo() {
        prodInfo.put("new_name", mProductNameView.getText().toString());
        if (mIsWrite.equals("1")) {
            prodInfo.put("new_nfc_id", mNfcIdView.getText().toString());
            prodInfo.put("new_tags", mTags.getItemAtPosition(0).toString());
            String s = "";
            for (int i = 0; i <= mSelectedItems.size() - 1; i++) {

                s += mSelectedItems.get(i) + ",";
            }
            prodInfo.put("new_ingredientId", s);
        } else {
            //TODO: FIX
            prodInfo.put("new_nfc_id", mNewNfcID);
            prodInfo.put("new_tags", "deli");
            prodInfo.put("new_ingredientId", "1");
        }
        prodInfo.put("new_product_id", mProductIdView.getText().toString());

        RequestHandler mRequestHandlerm3 = new RequestHandler(getApplicationContext(),
                this,
                "newProduct", prodInfo);
    }


    public void onResponse(String endpoint, String response) {
        if (endpoint.equals("product")) {
            parseProductData(response);
            Log.e(TAG, "product: " + mProductData);
        }

        if (endpoint.equals("ingredientList")) {
            populateIngredientsData(response);
            Log.e(TAG, "ingredientList: " + mProductData);

        }

        if (endpoint.equals("newProduct") && mIsWrite.equals("1")) {
            Intent allProd = new Intent(OrgWriteEditProduct.this, UserLandingPage.class);
            allProd.putExtra("BTN_PRESSED", "1");
            startActivity(allProd);
        }

        if (endpoint.equals("newNFCId")) {

            try {
                JSONObject productJsonObj = new JSONObject(response);
                mNewNfcID = productJsonObj.getString("nfc_id");
                Log.e(TAG, "Resp: " + productJsonObj.getString("nfc_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[]{tagDetected};
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage message = createTextMessage(mNewNfcID);
            Log.e(TAG,"NEW NFC ID: "+ mNewNfcID);
            if (writeTag(message, detectedTag)) {
                Toast.makeText(this, "Success in writing to the NFC tag", Toast.LENGTH_LONG)
                        .show();

                editProductInfo();
            }
        }
    }

    /*
     * Writes an NdefMessage to a NFC tag
     */
    public boolean writeTag(NdefMessage message, Tag tag) {

        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }


    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
