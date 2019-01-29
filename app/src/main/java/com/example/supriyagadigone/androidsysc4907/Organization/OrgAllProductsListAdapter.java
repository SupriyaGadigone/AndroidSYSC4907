package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrgAllProductsListAdapter extends BaseAdapter {

    private static String TAG = "OrgAllProductsListAdapter";
    Context context;
    private Map<String,String> productData;
    private boolean mIsCustomer;
    private Map<String, String> flagsMap;

    public OrgAllProductsListAdapter(Context context, Map<String,String> prodData, boolean mIsCustomer,  Map<String, String> flagsMap) {
        this.context = context;
        this.productData = prodData;
        this.mIsCustomer = mIsCustomer;
        this.flagsMap = flagsMap;

    }

    @Override
    public int getCount() {
        return productData.size();
    }

    @Override
    public Object getItem(int position) {
        return productData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_all_products_row, parent, false);



        TextView productName =  vi.findViewById(R.id.product_name);
        LinearLayout editProduct = vi.findViewById(R.id.edit_product);

        String productNameText = (String) productData.keySet().toArray()[position];
        productName.setText(productNameText);

        if(mIsCustomer){
            editProduct.setVisibility(View.GONE);
            if(flagsMap.containsKey(productNameText.replace(" ", ""))){
                vi.setBackgroundColor(Color.parseColor("#DB7093"));
            }
        }

        return vi;
    }
}