package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseProductInfo {

    private static String TAG = "ParseProductInfo";

    public View parseProductData(String response, View v, Context c) {
        Log.e(TAG, response);

        String[] tagItems;
        TextView mProductNameView = v.findViewById(R.id.product_name);
        TextView mNfcIdView = v.findViewById(R.id.nfc_id);
        TextView  mProductIdView = v.findViewById(R.id.product_id);
        Spinner mTags = v.findViewById(R.id.tags_options);

        try {
           // JSONArray prdArray = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(response);
            JSONObject productJsonObj2 = new JSONObject(productJsonObj.getString("product"));
            mProductNameView.setText(productJsonObj2.getString("name"));
           // mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj2.getString("product_id"));
            tagItems = new String[]{productJsonObj2.getString("tags")}; //TODO: get list of tags


            ArrayAdapter<String> adapter = new ArrayAdapter<>(c, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }
}
