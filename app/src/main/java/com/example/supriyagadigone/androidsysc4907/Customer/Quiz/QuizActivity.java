package com.example.supriyagadigone.androidsysc4907.Customer.Quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.supriyagadigone.androidsysc4907.Customer.RestrictionsQuizHandler;
import com.example.supriyagadigone.androidsysc4907.LoginActivity;
import com.example.supriyagadigone.androidsysc4907.OnResponseCallback;
import com.example.supriyagadigone.androidsysc4907.Organization.OrgWriteEditProduct;
import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.RequestHandler;
import com.example.supriyagadigone.androidsysc4907.UserLandingPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class QuizActivity extends AppCompatActivity implements OnResponseCallback {

    public static final String PREFS_NAME = "PREFS_FILE";
    private final String TAG = "QuizActivity";

    private RecyclerView mRecyclerView;
    private QuizAdapter mQuizAdapter;
    private Button mNextButton;

    private Map<String, String> ingridentsData;
    private ArrayList<Integer> mSelectedItems;
    private AlertDialog.Builder mIngridientsBuilder;
    private Button mIngredientsButton;
    private RestrictionsQuizHandler mRestrictionsQuizHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_grid);

        mRecyclerView = findViewById(R.id.recycler_View);
        ingridentsData = new HashMap<>();
        mIngridientsBuilder = new AlertDialog.Builder(QuizActivity.this);
        mIngredientsButton = findViewById(R.id.ingredients_list);
        mRestrictionsQuizHandler = new RestrictionsQuizHandler();

        Map<String, String> quizData = new HashMap<String, String>();
        quizData.put("flag", "tags");

        RequestHandler mRequestHandlerm = new RequestHandler(getApplicationContext(),
                QuizActivity.this,
                "restrictions", quizData);


        RequestHandler mRequestHandlerm1 = new RequestHandler(getApplicationContext(),
                this,
                "ingredientList");

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setRestrictions();
                Intent allCust = new Intent(QuizActivity.this, UserLandingPage.class);
                allCust.putExtra("BTN_PRESSED", "0");
                startActivity(allCust);
            }
        });

    }

    private void parseQuizValues(String response){
       // Log.e(TAG, response);

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

    public void onResponse(String endpoint, String response) {

        if (endpoint.equals("restrictions")) {
            parseQuizValues(response);
        }

        if (endpoint.equals("ingredientList")) {
            mRestrictionsQuizHandler.populateIngredientsData(response, this, mIngredientsButton);
        }

    }
}
