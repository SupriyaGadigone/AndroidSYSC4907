package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.Customer.CustomerTappedProductInfo;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProductsListAdapter;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgWriteEditProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserLandingPage extends BaseActivity implements OnResponseCallback {

    private static String TAG = "UserLandingPage";

    private Map<String, String> productData;

    private TextView emptyProd;
    private ListView allProductsList;

    private String mCustOrgBtn;
    private Map<String, String> flagsMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.products_list, frameLayout);


        productData = new HashMap<>();
        emptyProd = findViewById(R.id.empty_products);
        //TODO: once empty, write to NFC option should show up
        flagsMap = new HashMap<>();

        allProductsList = findViewById(R.id.listProducts);

        initToolbar();

        mCustOrgBtn = getIntent().getStringExtra("BTN_PRESSED");

        if (mCustOrgBtn.equals("0")) {

            setData("Tapped Products", "product");
            mIsCustomer = true;

        } else {

            setData("All Products", "productList");
            mIsCustomer = false;
        }


    }

    private void setData(String title, String endpoint) {
        toolbar.setTitle(title);

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                endpoint);

        allProductsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (mIsCustomer) {
                    intent = new Intent(UserLandingPage.this, CustomerTappedProductInfo.class);
                    String prodName = (String) productData.keySet().toArray()[position];
                    if(flagsMap.containsKey(prodName.replace(" ", ""))){
                        intent.putExtra("FLAG", "0");
                    }
                    intent.putExtra("PROD_DATA", (String) productData.get(productData.keySet().toArray()[position]));

                } else {
                    intent = new Intent(UserLandingPage.this, OrgWriteEditProduct.class);
                    intent.putExtra("IS_WRITE", "1");
                    intent.putExtra("PROD_DATA", productData.get(productData.keySet().toArray()[position]));
                }

                startActivity(intent);
            }
        });

    }

    public void parseProductsData(String response) {
        try {
            if (mIsCustomer) {
                JSONObject productJsonObj = new JSONObject(response);
                response = productJsonObj.getString("TappedProducts");
                productData.put("flag", productJsonObj.getString("flag"));


                String flags = productJsonObj.getString("flag");
                String[] flagsArrayindex = flags.split(",");
                Log.e(TAG, "Flag size: " + flags.length());
                for (int i = 0; i < flagsArrayindex.length; i++) {
                    String str = flagsArrayindex[i].replace("'", "");
                    String str2 = str.replace(" ", "");
                    Log.e(TAG, "str: " + str2);
                    flagsMap.put(str2, "");
                }
            }
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                productData.put(productJsonObj.getString("name"), productJsonObj.toString());
            }

            OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(UserLandingPage.this, productData, mIsCustomer, flagsMap);
            if (productData.size() == 0) {
                emptyProd.setVisibility(View.VISIBLE);
            }
            allProductsList.setAdapter(adapter);




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onResponse(String endpoint, String response) {
        parseProductsData(response);
    }

}
