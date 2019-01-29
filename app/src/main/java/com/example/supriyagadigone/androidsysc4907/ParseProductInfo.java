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
    private Map<String,String> ingridentsData;
    private boolean[] isChecked;
    private ArrayList<Integer> mSelectedItems;
    private Button mIngredientsButton;
    String[] tagItems;


    public ParseProductInfo(Context c){
        this.mContext = c;
        RequestHandler mRequestHandlerm1 = new RequestHandler(mContext,
                this,
                "ingredientList");
        ingridentsData = new HashMap<>();
        mIngredientsButton = new Button(c);
        mIngredientsButton.findViewById(R.id.ingredients_list);
        tagItems = new String[]{"MEAT","PRODUCE","ORGANIC","DELI","SEAFOOD", "GROCERY", "BAKERY"};
    }

    public View parseProductData(String response, View v) {
        Log.e(TAG, response);


        TextView mProductNameView = v.findViewById(R.id.product_name);
        //TextView mNfcIdView = v.findViewById(R.id.nfc_id);
        TextView  mProductIdView = v.findViewById(R.id.product_id);
        Spinner mTags = v.findViewById(R.id.tags_options);

        try {
           // JSONArray prdArray = new JSONArray(response);
            JSONObject productJsonObj = new JSONObject(response);
            JSONObject productJsonObj2 = new JSONObject(productJsonObj.getString("product"));
            mProductNameView.setText(productJsonObj2.getString("name"));
           // mNfcIdView.setText(productJsonObj.getString("nfc_id"));
            mProductIdView.setText(productJsonObj2.getString("product_id"));

            parseInfo(productJsonObj2.getJSONArray("ingredient"));
            //TODO: get list of tags

            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, tagItems);
            mTags.setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(productJsonObj2.getString("tags"));
            mTags.setSelection(spinnerPosition);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }


    public void onResponse(String endpoint, String response) {
        populateIngredientsData(response);
    }

    public void populateIngredientsData(String response) {
        Log.e(TAG,"populateIngredientsData: "+ response);
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
        Log.e(TAG,"Here3");
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
        final AlertDialog.Builder mIngridientsBuilder = new AlertDialog.Builder(mContext);
        Log.e(TAG,"Here4");
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
