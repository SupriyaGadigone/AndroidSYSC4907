package com.example.supriyagadigone.androidsysc4907;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParseProductInfo implements OnResponseCallback{

    private static String TAG = "ParseProductInfo";
    private Context mContext;
    private boolean[] isChecked;
    private ArrayList<Integer> mSelectedItems;
    private Button mIngredientsButton;
    String[] tagItems;


    private JSONArray mIngrJsonData;

    public ParseProductInfo(Context c){
        this.mContext = c;
        mIngrJsonData = new JSONArray();
        tagItems = new String[]{"MEAT","PRODUCE","ORGANIC","DELI","SEAFOOD", "GROCERY", "BAKERY"};
    }

    public View parseProductData(String response, View v) {
        Log.e(TAG, "parseProductData(): " + response);
        TextView mProductNameView = v.findViewById(R.id.product_name);
        //TextView mNfcIdView = v.findViewById(R.id.nfc_id);
        TextView  mProductIdView = v.findViewById(R.id.product_id);
        Spinner mTags = v.findViewById(R.id.tags_options);
        mIngredientsButton = v.findViewById(R.id.ingredients_list);

        try {
            JSONObject productJsonObj = new JSONObject(response);
            JSONObject productJsonObj2 = new JSONObject(productJsonObj.getString("product"));
            mProductNameView.setText(productJsonObj2.getString("name"));
            mProductIdView.setText(productJsonObj2.getString("product_id"));

            mIngrJsonData = productJsonObj2.getJSONArray("ingredient");


            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(productJsonObj2.getString("tags"));
            mTags.setSelection(spinnerPosition);

            RequestHandler mRequestHandlerm1 = new RequestHandler(mContext,
                    this,
                    "ingredientList");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }


    public void onResponse(String endpoint, String response) {
        populateIngredientsData(response);
    }

    public void populateIngredientsData(String response) {

        Map<String,String> ingridentsData = new HashMap<>();
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                ingridentsData.put(productJsonObj.getString("name"), productJsonObj.getString("ingredient_id"));
            }
            parseInfo(mIngrJsonData, ingridentsData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseInfo(JSONArray ingrJsonData, Map<String,String>  ingridentsData) throws JSONException {
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
        setIngredientsButton(ingridentsData);
    }

    private void setIngredientsButton(final Map<String,String> ingridentsData) {
        final AlertDialog.Builder mIngridientsBuilder = new AlertDialog.Builder(mContext);
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
                                        } else if (mSelectedItems.contains(ingridentsData.get(ingridentsData.keySet().toArray()[i]))) {
                                            // Else, if the item is already in the array, remove it
                                            mSelectedItems.remove(Integer.parseInt(ingridentsData.get(ingridentsData.keySet().toArray()[i])));
                                        }
                                    }

                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });

                AlertDialog mDialog = mIngridientsBuilder.create();
                mDialog.show();
            }
        });


    }
}
