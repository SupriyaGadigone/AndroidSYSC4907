package com.example.supriyagadigone.androidsysc4907.Customer;

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
import java.util.List;

public class CustomerGroceryList extends BaseActivity implements OnResponseCallback {

    private static String TAG = "CustomerGroceryList";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private String  listData;
    private Button mAddBtn;

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
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        listData = getIntent().getStringExtra("LIST_DATA");

        if(listData!=null) {
            parseListData(listData);
            Log.e(TAG, "********: " + listData);
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
    }

    public void onSaveList(View v){
             
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
        try {
            JSONArray shoppingListsArray = new JSONArray(response);
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
        try {
        JSONArray jsonData = new JSONArray(response);
            try {
                for (int i = 0; i < jsonData.length(); i++) {

                    JSONObject productJsonObj = new JSONObject(jsonData.get(i).toString());
                    productsList.add(productJsonObj.getString("name"));
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
}
