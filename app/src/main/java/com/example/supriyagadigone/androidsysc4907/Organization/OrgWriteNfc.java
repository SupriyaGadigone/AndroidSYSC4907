package com.example.supriyagadigone.androidsysc4907.Organization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to write NFC tags with own mimetype and ID
 * Based on the excellent tutorial by Jesse Chen
 * http://www.jessechen.net/blog/how-to-nfc-on-the-android-platform/
 * https://github.com/balloob/Android-NFC-Tag-Writer/blob/master/src/nl/paulus/nfctagwriter/MainActivity.java
 */
public class OrgWriteNfc extends BaseActivity implements OnResponseCallback {

    private static String TAG = "OrgWriteNfc";

    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;

    private EditText mProductNameView;
    private EditText mProductIdView;
    private Spinner mTags; //TODO:get list of tags
    private Button mIngredientsButton;
    private String nfcID;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.org_write_nfc_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Write to NFC");
        mIsCustomer = false;

        RequestHandler mRequestHandlerm3 = new RequestHandler(getApplicationContext(),
                this,
                "newNFCId");



        (findViewById(R.id.write_to_NFC)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNfcAdapter = NfcAdapter.getDefaultAdapter(OrgWriteNfc.this);
                mNfcPendingIntent = PendingIntent.getActivity(OrgWriteNfc.this, 0,
                        new Intent(OrgWriteNfc.this, OrgWriteNfc.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                enableTagWriteMode();

                new AlertDialog.Builder(OrgWriteNfc.this).setTitle("Touch tag to write")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                disableTagWriteMode();
                            }

                        }).create().show();
            }
        });
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
            NdefMessage message = createTextMessage(nfcID);
            if (writeTag(message, detectedTag)) {
                Toast.makeText(this, "Success in writing to the NFC tag", Toast.LENGTH_LONG)
                        .show();
                newProductInfo();
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


    private void newProductInfo() {
        //dosable write button until get new nfc_id
        Map<String, String> prodInfo;
        prodInfo = new HashMap<>();

        mProductNameView = findViewById(R.id.product_name);
        // mNfcIdView = findViewById(R.id.nfc_id);
        mProductIdView = findViewById(R.id.product_id);

        prodInfo.put("new_name", mProductNameView.getText().toString());
        prodInfo.put("new_nfc_id", nfcID);
        prodInfo.put("new_product_id", mProductIdView.getText().toString());
        prodInfo.put("new_tags", "deli");
        prodInfo.put("token", mToken);
       // prodInfo.put("token", mToken);

        // Log.e(TAG, "SIZE3: " + mSelectedItems.size());
//        String s = "";
//        for(int i = 0; i<=mSelectedItems.size()-1 ; i++){
//            // Log.e(TAG, "SELECTED: " + mSelectedItems.get(i));
//            // Log.e(TAG, "i: " + i);
//            s+= mSelectedItems.get(i) + ",";
//        }
       prodInfo.put("new_ingredientId", "1,3");

        RequestHandler mRequestHandlerm3 = new RequestHandler(getApplicationContext(),
                this,
                "newProduct", prodInfo);
    }




    public void onResponse(String endpoint, String response) {
        if(endpoint.equals("newNFCId")) {

            try {
                JSONObject productJsonObj = new JSONObject(response);
                nfcID = productJsonObj.getString("nfc_id");
                Log.e(TAG, "Resp: " + productJsonObj.getString("nfc_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if(endpoint.equals("newProduct")){
            Log.e(TAG, "haha");
        }
    }
}