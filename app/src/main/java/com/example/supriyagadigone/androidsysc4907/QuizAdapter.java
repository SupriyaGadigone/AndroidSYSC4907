package com.example.supriyagadigone.androidsysc4907;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {
    private String[] subjectValues;


    public QuizAdapter (String[] subjectValues){
        this.subjectValues = subjectValues;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mFoodRestrictionText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFoodRestrictionText = itemView.findViewById(R.id.food_restriction_name);
        }
    }
}
