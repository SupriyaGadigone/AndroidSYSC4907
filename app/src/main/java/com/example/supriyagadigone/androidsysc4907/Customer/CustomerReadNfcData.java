package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.RestrictionsData;
import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

public class CustomerReadNfcData extends BaseActivity {

    private static final String TAG = "CustomerReadNfcData";

    private TextView mProductName;
    private TextView mIngredients;
    private TextView mWarning;
    private boolean mWarningRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_read_nfc_info, frameLayout);
        initToolbar();
        mIsCustomer = true;

        this.mWarningRequired = false;

        Bundle b = getIntent().getExtras();
        String nfcData = "";

        if (b != null) {
            nfcData = b.getString("nfcdata");
            try {
                JSONArray jsonNfcData = new JSONArray(nfcData);

                mProductName = findViewById(R.id.product_name_nfc);
                mProductName.setText(jsonNfcData.get(0).toString());

                setIngredientsData(jsonNfcData);

                if (mWarningRequired) {
                    mWarning = findViewById(R.id.warning_nfc);
                    mWarning.setText(R.string.warning_string);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setIngredientsData(JSONArray jsonObject) throws JSONException {
        RestrictionsData savedRestrictions[] = RestrictionsData.values();
        SharedPreferences prefs = getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        mIngredients = findViewById(R.id.ingredients_nfc);
        String ingredientNames = "";

        for (int i = 1; i < jsonObject.length(); i++) {
            ingredientNames += jsonObject.get(i) + ", ";
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (((savedRestrictions[Integer.parseInt(entry.getKey())]).toString().toLowerCase()).equals(jsonObject.get(i).toString().toLowerCase())) {
                    mWarningRequired = true;
                }
            }
        }
        //TODO: fix this, it wont work
        mIngredients.setText(ingredientNames);
    }
}
