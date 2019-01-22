package com.example.supriyagadigone.androidsysc4907;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.Customer.CustomerTappedProductInfo;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgAllProductsListAdapter;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgEditProduct;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.products_list, frameLayout);


        productData = new HashMap<>();
        emptyProd = findViewById(R.id.empty_products);
        //TODO: once empty, write to NFC option should show up


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
                } else {
                    intent = new Intent(UserLandingPage.this, OrgEditProduct.class);
                }
                intent.putExtra("PROD_DATA", productData.get(productData.keySet().toArray()[position]));
                startActivity(intent);
            }
        });

    }

    public void parseProductsData(String response) {
        try {
            JSONArray jsonData = new JSONArray(response);
            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                productData.put(productJsonObj.getString("name"), productJsonObj.toString());
            }
            OrgAllProductsListAdapter adapter = new OrgAllProductsListAdapter(UserLandingPage.this, productData, mIsCustomer);
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
