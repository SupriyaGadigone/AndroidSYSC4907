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
import com.example.supriyagadigone.androidsysc4907.Customer.CustomerReadNfcData;
import com.example.supriyagadigone.androidsysc4907.Customer.CustomerReadNfcDataFeatchr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * References:
 * https://www.codexpedia.com/android/android-nfc-read-and-write-example/
 * https://code.tutsplus.com/tutorials/reading-nfc-tags-with-android--mobile-17278
 */
public class ReadNfc extends BaseActivity implements OnResponseCallback{

    private static final String TAG = "ReadNfc";
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private Context mContext;
    private NfcAdapter mNfcAdapter;
    private TextView mNfcDataView;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater factory;

    private RequestQueue mRequestQueue;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.read_nfc_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Read NFC");

        mContext = this;
        //TODO: change this to NFC status view
        mNfcDataView = findViewById(R.id.textView_explanation);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        factory = LayoutInflater.from(mContext);
        alertDialogBuilder = new AlertDialog.Builder(mContext);

        // set title
        alertDialogBuilder.setTitle("Ready to Scan");

//        // set dialog message
//        alertDialogBuilder.setMessage("Place device near NFC product tag").setCancelable(true);

        if (mNfcAdapter == null) {
            Toast.makeText(this.mContext, "This device doesnt support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!mNfcAdapter.isEnabled()) {
            mNfcDataView.setText(R.string.nfc_disabled);
        } else {
            mNfcDataView.setText(R.string.nfc_enabled);

            final View view = factory.inflate(R.layout.ready_to_scan_dialog, null);
            alertDialogBuilder.setView(view);

            // create alert dialog
            alertDialog = alertDialogBuilder.create();

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            // show it
            alertDialog.show();
        }

        handleIntent(getIntent());

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



//    /**
//     * Background process to fetch data.
//     */
//    private class FetchProductData extends AsyncTask<Void, Void, Void> {
//
//        private String mNfcId;
//
//
//        public FetchProductData(String nfcId) {
//            this.mNfcId = nfcId;
//        }
//
//        /**
//         * Background thread
//         */
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                String result = new CustomerReadNfcDataFeatchr().getUrlString(
//                        "http://74.12.190.176:8000/nfcid/" + mNfcId +"/1");
//
//                Intent intent = new Intent(ReadNfc.this, CustomerReadNfcData.class);
//                Bundle b = new Bundle();
//                b.putString("nfcdata", result);
//                intent.putExtras(b);
//                startActivity(intent);
//                finish();
//
//                Log.e(TAG, "Passed to fetch data");
//            } catch (IOException ioe) {
//                //TODO: make a alert box when cant fetch data
//                Log.e(TAG, "Failed to fetch data", ioe);
//            }
//
//            return null;
//        }
//
//    }

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
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1
             *
             * http://www.nfc-forum.org/specs/
             *
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                alertDialog.dismiss();



                mNfcDataView.setText("Read content: " + result);
                displayProductInfo(result);
                //new FetchProductData("" + result).execute();
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

    public void displayProductInfo(String result){
        Log.e(TAG, "NFC id: " + result);
        mRequestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        Map<String, String> prodInfo = new HashMap<>();

        setUserCredentials();

        prodInfo.put("nfc_id",result);
        prodInfo.put("token",mToken);
        RequestHandler mRequestHandlerm1 = new RequestHandler(mRequestQueue,
                this,
                "product", prodInfo);
    }

    public void onResponse(String endpoint, String response) {
        //display on the pop up
        Log.e(TAG, "PROD INFO in READ: " + response);

        parseProductData(response);

    }

    public void setUserCredentials() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);

        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            if (entry.getKey().equals("token")) {
                mToken = entry.getValue().toString();
            }
        }
    }


    public void parseProductData(String response) {
        Log.e(TAG, "PROD: " + response);
        final View view2 = factory.inflate(R.layout.customer_tapped_products_info, null);
         TextView mProductNameView;
         TextView mNfcIdView;
         TextView mProductIdView;
         Spinner mTags; //TODO:get list of tags
        //  private Button mIngredientsButton;
         String[] tagItems;

        mProductNameView = view2.findViewById(R.id.product_name);
        mNfcIdView = view2.findViewById(R.id.nfc_id);
        mProductIdView = view2.findViewById(R.id.product_id);

        mTags = view2.findViewById(R.id.tags_options);

        try {
           // JSONArray jsonData = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(response);
            Log.e(TAG, "NAAAAME: "+productJsonObj.getString("name"));
            mProductNameView.setText(productJsonObj.getString("name"));
            mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj.getString("product_id"));
            tagItems = new String[]{productJsonObj.getString("tags")};

//            JSONArray ingrJsonData = new JSONArray(productJsonObj.getString("ingredient"));
//            parseInfo(ingrJsonData);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        alertDialogBuilder.setView(view2);
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

}
