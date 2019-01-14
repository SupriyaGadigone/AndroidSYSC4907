package com.example.supriyagadigone.androidsysc4907.Organization;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.R;

import java.util.Map;

public class OrgAllProductsListAdapter extends BaseAdapter {

    private static String TAG = "OrgAllProductsListAdapter";
    Context context;
    Map<String,String> pIVals;
    private static LayoutInflater inflater = null;
    private boolean mIsCustomer;

    public OrgAllProductsListAdapter(Context context, Map<String,String> pIVals, boolean mIsCustomer) {
        this.context = context;
        this.pIVals = pIVals;
        this.mIsCustomer = mIsCustomer;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pIVals.size();
    }

    @Override
    public Object getItem(int position) {
        return pIVals.get(position);
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
        TextView productIngredients = vi.findViewById(R.id.product_ingredients);
        LinearLayout editProduct = vi.findViewById(R.id.edit_product);

        if(mIsCustomer){
            editProduct.setVisibility(View.GONE);
        }

        String productNameText = (String) pIVals.keySet().toArray()[position];
        String productIngredientsText = pIVals.get(productNameText);

        productName.setText(productNameText);
        productIngredients.setText(productIngredientsText);

            //TODO:edit NFC

        return vi;
    }
}