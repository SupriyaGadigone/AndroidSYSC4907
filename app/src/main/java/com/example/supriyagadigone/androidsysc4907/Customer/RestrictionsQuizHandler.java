package com.example.supriyagadigone.androidsysc4907.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizAdapter;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestrictionsQuizHandler {

    private static String TAG = "RestrictionsQuizHandler";

    private Map<String, String> ingridentsData;
    private Map<String, String> storeData;
    private ArrayList<Integer> mSelectedItems = new ArrayList<>();


    public void populateIngredientsData(String response, Context c, Button mIngredientsButton) {
        //    Log.e(TAG,"Here2");
        ingridentsData = new HashMap<>();
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                ingridentsData.put(productJsonObj.getString("name"), productJsonObj.getString("ingredient_id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

         setIngredientsButton(c, mIngredientsButton);
    }

    public void populateStoreData(String response, Context c, Button storeButton){
        storeData = new HashMap<>();
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                storeData.put(productJsonObj.getString("name"), productJsonObj.getString("org_id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String[] items = storeData.keySet().toArray(new String[storeData.keySet().size()]);
        final AlertDialog.Builder mStoreBuilder = new AlertDialog.Builder(c);

        SharedPreferences prefs = c.getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoreBuilder.setTitle("Choose Store");
                mStoreBuilder
                        .setSingleChoiceItems(items, 0, null)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                Log.e(TAG, "slected pos: "+selectedPosition);
                                Log.e(TAG, "slected pos2: "+storeData.get(items[selectedPosition]));
                                editor.putString("org_id", "" + storeData.get(items[selectedPosition]));
                                editor.apply();
                             }
                        });

                AlertDialog mDialog = mStoreBuilder.create();
                mDialog.show();
            }
        });



    }


    private void setIngredientsButton(Context c, Button mIngredientsButton) {

        final String[] items = ingridentsData.keySet().toArray(new String[ingridentsData.keySet().size()]);
        final AlertDialog.Builder mIngridientsBuilder = new AlertDialog.Builder(c);

        mIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIngridientsBuilder.setTitle("Choose Ingredients");
                mIngridientsBuilder
                        .setMultiChoiceItems(items, null,
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

    public Map<String, String> getIngridentsData() {
        return ingridentsData;
    }

    public void setIngridentsData(Map<String, String> ingridentsData) {
        this.ingridentsData = ingridentsData;
    }

    public ArrayList<Integer> getSelectedItems() {
        return mSelectedItems;
    }

    public void setSelectedItems(ArrayList<Integer> selectedItems) {
        mSelectedItems = selectedItems;
    }


//    private void setRestrictions(){
//        String s = "";
//        for (int i = 0; i <= mSelectedItems.size() - 1; i++) {
//
//            s += mSelectedItems.get(i) + ",";
//        }
//        // Log.e(TAG, "string"+s);
//        Map<String, String> customRest = new HashMap<String, String>();
//        customRest.put("restrict", s);
//
//        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
//                this,
//                "restrictions", customRest);
//    }
//
//    public void onResponse(String endpoint, String response) {
//
//
//        if (endpoint.equals("ingredientList")) {
//            populateIngredientsData(response);
//        }
//
//    }
}
