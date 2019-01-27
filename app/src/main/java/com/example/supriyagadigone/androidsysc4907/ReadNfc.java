package com.example.supriyagadigone.androidsysc4907;

import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * References:
 * https://www.codexpedia.com/android/android-nfc-read-and-write-example/
 * https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
 */
public class ReadNfc extends BaseActivity implements OnResponseCallback {

    private static final String TAG = "ReadNfc";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private Context mContext;
    private NfcAdapter mNfcAdapter;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater factory;

    private TextView mNfcDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.read_nfc_page, frameLayout);

        initToolbar();
        toolbar.setTitle("Read NFC");

        mContext = this;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        setUpReadNFCAlertDialog();

        handleIntent(getIntent());

    }

    private void setUpReadNFCAlertDialog() {
        //TODO: change this to NFC status view
        mNfcDataView = findViewById(R.id.textView_explanation);
        mNfcDataView.setVisibility(View.GONE);

        factory = LayoutInflater.from(mContext);

        alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Ready to Scan");

        if (mNfcAdapter == null) {
            Toast.makeText(this.mContext, "This device doesnt support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!mNfcAdapter.isEnabled()) {
          //  mNfcDataView.setText(R.string.nfc_disabled); //TODO: UI for this
        } else {
         //   mNfcDataView.setText(R.string.nfc_enabled);

            final View view = factory.inflate(R.layout.ready_to_scan_dialog, null);
            alertDialogBuilder.setView(view);

            alertDialog = alertDialogBuilder.create();
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();
        }
    }


    public void displayProductInfo(String result) {
        Log.e(TAG, "NFC id: " + result);

        Map<String, String> prodInfo = new HashMap<>();

        prodInfo.put("nfc_id", result);
        RequestHandler mRequestHandlerm1 = new RequestHandler(getApplicationContext(),
                this,
                "product", prodInfo);
    }

    public void onResponse(String endpoint, String response) {
        Log.e(TAG, "PROD INFO in READ: " + response);
        parseProductData(response);

    }

    public void parseProductData(String response) {
        View v = factory.inflate(R.layout.customer_tapped_products_info, null);

        //TODO:get list of tags
        //TODO: flag and warning here

        ParseProductInfo p = new ParseProductInfo();

        alertDialogBuilder.setView(p.parseProductData( response , v, this));

        alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Product Information");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            }
        });

        // show it
        alertDialog.show();
    }

    private void handleIntent(Intent intent) {

        String action = intent.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord[] records = ndefMessage.getRecords();

            for (NdefRecord ndefRecord : records) {
                try {
                    return readText(ndefRecord);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            }
            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {

            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 0063;

            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                alertDialog.dismiss();

               // mNfcDataView.setText("NFC ID: " + result);
                displayProductInfo(result);
            }
        }
    }


    /**
     * @param activity The corresponding requesting to stop the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

}
