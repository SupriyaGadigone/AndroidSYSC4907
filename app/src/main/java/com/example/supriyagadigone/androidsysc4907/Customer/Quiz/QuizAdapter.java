package com.example.supriyagadigone.androidsysc4907.Customer.Quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import java.util.HashMap;
import java.util.Map;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    private  final String TAG = "QuizAdapter";

    private String[] mRestrictionsDataValues;
    private Context mContext;
    private SharedPreferences prefs;


    public QuizAdapter (String[] restrictionsDataValues, Context context){
        this.mRestrictionsDataValues = restrictionsDataValues;
        this.mContext = context;

        prefs = mContext.getSharedPreferences(QuizActivity.PREFS_NAME, Context.MODE_PRIVATE);
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



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnResponseCallback{

        private TextView mFoodRestrictionText;

        public MyViewHolder(View itemView) {
            super(itemView);
            mFoodRestrictionText = itemView.findViewById(R.id.food_restriction_name);
            mFoodRestrictionText.setOnClickListener(this);
        }

        public void onClick(View view) {
            Map<String, String> quizData = new HashMap<String, String>();
            Log.e(TAG,"blah: "+ mRestrictionsDataValues[getAdapterPosition()]);
            quizData.put("restrict", mRestrictionsDataValues[getAdapterPosition()]);

            RequestHandler mRequestHandlerm = new RequestHandler(mContext,
                    this,
                    "restrictions", quizData);
            //TODO: move into string and one request then
            view.setEnabled(false);

        }

        public void onResponse(String endpoint, String response) {

        }
    }
}
