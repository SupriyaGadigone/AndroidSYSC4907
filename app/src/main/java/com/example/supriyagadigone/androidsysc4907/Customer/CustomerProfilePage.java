package com.example.supriyagadigone.androidsysc4907.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.supriyagadigone.androidsysc4907.BaseActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizAdapter;
import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.RestrictionsData;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerProfilePage extends BaseActivity implements OnResponseCallback {

    private ListView restrictionsList;
    private final String TAG = "CustomerProfilePage";
    private QuizAdapter mQuizAdapter;
    private RecyclerView mRecyclerView;
    private RestrictionsQuizHandler mRestrictionsQuizHandler;
    private Button mIngredientsButton;
    private ArrayList<Integer> mSelectedItems;
    private Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.customer_profile_page, frameLayout);
        initToolbar();
        toolbar.setTitle("Profile");
        mIsCustomer = true;

        mRecyclerView = findViewById(R.id.recycler_View);
        mRestrictionsQuizHandler = new RestrictionsQuizHandler();
        mIngredientsButton = findViewById(R.id.ingredients_list);

        Map<String, String> quizData = new HashMap<String, String>();
        quizData.put("flag", "tags");

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                CustomerProfilePage.this,
                "restrictions", quizData);

        RequestHandler mRequestHandlerm1 = new RequestHandler(getApplicationContext(),
                this,
                "ingredientList");

        List<RestrictionsData> restDataList = Arrays.asList(RestrictionsData.values());


        mSaveButton = findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setRestrictions();

            }
        });

    }

    public void onResponse(String endpoint, String response) {

        if (endpoint.equals("restrictions")) {
            parseQuizValues(response);
        }

        if (endpoint.equals("ingredientList")) {
            mRestrictionsQuizHandler.populateIngredientsData(response, this, mIngredientsButton);
        }

    }

    private void parseQuizValues(String response){
        Log.e(TAG, response);

        try {
            JSONObject quizData = new JSONObject(response);
            String data = quizData.getString("tags");
            String[] quizArray = data.split(",", -1);
            mQuizAdapter = new QuizAdapter(quizArray, getApplicationContext());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mQuizAdapter);
    }

    private void setRestrictions(){
        String s = "";
        mSelectedItems = mRestrictionsQuizHandler.getSelectedItems();
        for (int i = 0; i <= mSelectedItems.size() - 1; i++) {

            s += mSelectedItems.get(i) + ",";
        }
        // Log.e(TAG, "string"+s);
        Map<String, String> customRest = new HashMap<String, String>();
        customRest.put("restrict", s);

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                this,
                "restrictions", customRest);
    }
}
