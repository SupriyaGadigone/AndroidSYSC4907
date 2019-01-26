package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Map;
import java.util.Random;

public class CustomerGroceryListPage extends BaseActivity implements OnResponseCallback {

    private static String TAG = "CustomerGroceryListPage";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private Map<String,String> shopListsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_grocery_list_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Grocery Lists");
        mIsCustomer = true;

        shopListsData = new HashMap<>();
        lvItems = findViewById(R.id.listItems);


        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "shoppingList");

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.addListEditText);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");

        Map<String, String> listInfo = new HashMap<>();
        Random rand = new Random();
        int num = rand.nextInt(50) + 1;
        listInfo.put("list_id",num+"");
        listInfo.put("new_name",etNewItem.getText().toString());
        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "shoppingList", listInfo);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }

                });
    }


    private void setupListViewGroceryList() {

        for (String name: shopListsData.keySet()){

            String key =name;
            String value = shopListsData.get(name);
            Log.e(TAG, key + " *:* " + value);


        }

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                    Intent myIntent = new Intent(view.getContext(), CustomerGroceryList.class);
                    //myIntent.putExtra("LIST_DATA",shopListsData.get(shopListsData.keySet().toArray()[position]));
                    startActivityForResult(myIntent, 0);
                }});
    }

    private void parseShoppingListsData(String response) {
        items = new ArrayList<String>();
        try {
            JSONArray shoppingListsArray = new JSONArray(response);
            for(int i = 0; i < shoppingListsArray.length(); i++) {
                JSONObject shoppingList = new JSONObject(shoppingListsArray.get(i).toString());
                items.add(shoppingList.getString("name"));
                shopListsData.put(shoppingList.getString("name"), shoppingList.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewGroceryList();

    }

    public void onResponse(String endpoint, String response) {
        Log.e(TAG, response);
        parseShoppingListsData(response);
    }
}
