package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerGroceryList extends BaseActivity implements OnResponseCallback {

    private static String TAG = "CustomerGroceryList";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private String listData;
    private Button mAddBtn;
    private Map<String, String> productData;
    private String listId;
    private String orgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_grocery_list, frameLayout);
        initToolbar();

        //TODO: get the list name clicked here
        //toolbar.setTitle("Grocery Lists");
        mIsCustomer = true;

        lvItems = findViewById(R.id.listItems);

        mAddBtn = findViewById(R.id.btnAddItem);
        mAddBtn.setEnabled(false);

        items = new ArrayList<String>();
        productData = new HashMap<>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        listData = getIntent().getStringExtra("LIST_DATA");
        Log.e(TAG, "********: " + listData);

        if (listData != null) {
            parseListData(listData);
        }

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "productList");

        setupListViewListener();
        setupListViewListenerOnClick();
    }

    public void onAddItem(View v) {
        AutoCompleteTextView productSearch = findViewById(R.id.addGroceryItemView);
        String itemText = productSearch.getText().toString();
        itemsAdapter.add(itemText);
        productSearch.setText("");

        Map<String, String> prodInfo = new HashMap<>();

        prodInfo.put("list_id", listId);
        prodInfo.put("product", productData.get(itemText));
        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "shoppingList", prodInfo);
    }

    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }

                });
    }

    // Attaches a long click listener to the listview
    private void setupListViewListenerOnClick() {
//TODO
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = lvItems.getItemAtPosition(position).toString();
                if(lvItems.getItemAtPosition(position).toString().contains("IN STOCK")){
                    Map<String, String> prodInfo = new HashMap<>();
//                    prodInfo.put("nfc_id", );
//                    RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
//                            CustomerGroceryList.this,
//                            "product", prodInfo);
                }
            }
        });
    }

    private void parseListData(String response) {
        //    Log.e(TAG, "parseListData(): "+ response);
        try {
            JSONObject shoppingListData = new JSONObject(response);
            JSONArray shoppingListsArray = new JSONArray(shoppingListData.getString("product"));
            listId = shoppingListData.getString("list_id");
            for (int i = 0; i < shoppingListsArray.length(); i++) {
                JSONObject shoppingList = new JSONObject(shoppingListsArray.get(i).toString());
                items.add(shoppingList.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSearchData(String response) {
        Log.e(TAG, response);
        List<String> productsList = new ArrayList<String>();
        //List<String> productsList = new ArrayList<String>();
        try {
            JSONArray jsonData = new JSONArray(response);
            try {
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                    productsList.add(productJsonObj.getString("name"));
                    productData.put(productJsonObj.getString("name"), productJsonObj.getString("product_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, productsList);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.addGroceryItemView);
        textView.setAdapter(adapter);
        mAddBtn.setEnabled(true);

        setListNfcData();

    }

    public void onResponse(String endpoint, String response) {
        Log.e(TAG, "respp: " + response);
        if (endpoint.equals("shoppingList") && response != "") {
            setNfcData(response);
        }

        if (endpoint.equals("productList")) {
            setSearchData(response);
        }

    }

    private void setNfcData(String response) {
        Log.e(TAG, "setNfcData: " + response);
        try {
            JSONArray jsonData = new JSONArray(response);

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject prodData = new JSONObject(jsonData.get(i).toString());
                JSONObject productnfcData = new JSONObject(prodData.getString("product"));
                Log.e(TAG, "Name: " + productnfcData.getString("name"));
                Log.e(TAG, "nfcid: " + prodData.getString("nfc_id"));
                if (!(prodData.getString("nfc_id").equals("False"))) {
                    Log.e(TAG, "nameee: " + items.indexOf(productnfcData.getString("name")));
                    int index = items.indexOf(productnfcData.getString("name"));
                    items.remove(index);
                    items.add(productnfcData.getString("name") + " [IN STORE]");
                    //TODO: add nfc id to a map and then on click get it, make a new one each time
                }
            }
            itemsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void onBackPressed() {
        Intent intent = new Intent(this, CustomerGroceryListPage.class);
        startActivity(intent);
    }

    private void setListNfcData() {
        Map<String, String> storeProdInfo = new HashMap<>();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(LoginActivity.LOGIN_PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, String> allEntries = (Map<String, String>) prefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.e(TAG, "key: " + entry.getKey());
            if (entry.getKey().equals("org_id")) {
                storeProdInfo.put("org_id", entry.getValue().toString());
                orgId = entry.getValue().toString();
            } else {
                storeProdInfo.put("org_id", "001");
                orgId = "001";
            }
        }

        storeProdInfo.put("list_id", listId);

        RequestHandler mRequestHandlerm2 = new RequestHandler(getApplicationContext(),
                this,
                "shoppingList", storeProdInfo);
    }
}
