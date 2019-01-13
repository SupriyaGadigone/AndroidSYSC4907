package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.R;

import java.util.Map;

public class OrgAllProductsListAdapter extends BaseAdapter {

    private static String TAG = "OrgAllProductsListAdapter";
    Context context;
    Map<String,String> pIVals;
    private static LayoutInflater inflater = null;

    public OrgAllProductsListAdapter(Context context, Map<String,String> pIVals) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.pIVals = pIVals;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pIVals.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pIVals.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_all_products_row, parent, false);

        TextView productName =  vi.findViewById(R.id.product_name);
        TextView productIngredients = vi.findViewById(R.id.product_ingredients);

        String productNameText = (String) pIVals.keySet().toArray()[position];
        String productIngredientsText = pIVals.get(productNameText);

        productName.setText(productNameText);
        productIngredients.setText(productIngredientsText);

            //TODO:edit

        return vi;
    }
}