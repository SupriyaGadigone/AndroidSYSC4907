package com.example.supriyagadigone.androidsysc4907;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {
    private String[] subjectValues;
    private static final String TAG = "QuizAdapter";
    private Context context;


    public QuizAdapter (String[] subjectValues, Context context){
        this.subjectValues = subjectValues;
        this.context = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mFoodRestrictionText.setText(subjectValues[position]);
    }

    public int getItemCount() {
        return subjectValues.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mFoodRestrictionText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFoodRestrictionText = itemView.findViewById(R.id.food_restriction_name);
            mFoodRestrictionText.setOnClickListener(this);
            addToSharedPreference(mFoodRestrictionText);
        }

        private void addToSharedPreference(TextView mFoodRestrictionText){
            SharedPreferences prefs = context.getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString(mFoodRestrictionText.getText() + "", mFoodRestrictionText.getText() + "");

            editor.commit();
        }

        public void onClick(View view) {
            Log.e(TAG, "onClick " + getAdapterPosition());
        }
    }
}
