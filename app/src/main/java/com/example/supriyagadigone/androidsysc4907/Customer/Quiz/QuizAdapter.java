package com.example.supriyagadigone.androidsysc4907.Customer.Quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.R;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    private  final String TAG = "QuizAdapter";

    private RestrictionsData[] mRestrictionsDataValues;
    private Context context;


    public QuizAdapter (RestrictionsData[] restrictionsDataValues, Context context){
        this.mRestrictionsDataValues = restrictionsDataValues;
        this.context = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mFoodRestrictionText.setText(mRestrictionsDataValues[position].toString());
    }

    public int getItemCount() {
        return mRestrictionsDataValues.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mFoodRestrictionText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFoodRestrictionText = itemView.findViewById(R.id.food_restriction_name);
            mFoodRestrictionText.setOnClickListener(this);
        }

        public void onClick(View view) {
            SharedPreferences prefs = context.getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getAdapterPosition() + "", getAdapterPosition() + "");
            view.setEnabled(false);
            editor.commit();
        }
    }
}
