package com.example.supriyagadigone.androidsysc4907.Customer;

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

public class CustomerGroceryList extends BaseActivity {

    private static String TAG = "CustomerGroceryList";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private String  listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_grocery_list, frameLayout);
        initToolbar();

        //TODO: get the list name clicked here
        //toolbar.setTitle("Grocery Lists");
        mIsCustomer = true;

        lvItems = findViewById(R.id.listItems);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        listData = getIntent().getStringExtra("LIST_DATA");

        parseListData(listData);
        Log.e(TAG, "********: "+listData);

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.addGroceryItemEditText);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
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


}
