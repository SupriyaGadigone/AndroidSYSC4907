package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Intent;
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
    private String  listData;
    private Button mAddBtn;
    private Map<String, String> productData;
    private String listId;


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

        if(listData!=null) {
            parseListData(listData);
        }

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "productList");

        setupListViewListener();
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

    private void parseListData(String response){
        Log.e(TAG, "parseListData(): "+ response);
        try {
            JSONObject shoppingListData = new JSONObject(response);
            JSONArray shoppingListsArray = new JSONArray(shoppingListData.getString("product"));
            listId = shoppingListData.getString("list_id");
            for(int i = 0; i < shoppingListsArray.length(); i++) {
                JSONObject shoppingList = new JSONObject(shoppingListsArray.get(i).toString());
                items.add(shoppingList.getString("name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSearchData(String response){
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
    }
    public void onResponse(String endpoint, String response) {
        setSearchData(response);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(this, CustomerGroceryListPage.class);
        startActivity(intent);
    }
}
