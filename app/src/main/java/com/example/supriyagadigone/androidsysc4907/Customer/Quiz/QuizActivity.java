package com.example.supriyagadigone.androidsysc4907.Customer.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.supriyagadigone.androidsysc4907.Customer.CustomerLandingPage;
import com.example.supriyagadigone.androidsysc4907.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class QuizActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private QuizAdapter mQuizAdapter;
    private Button mNextButton;
    private static final String TAG = "QuizActivity";

    //convert to enums
    private String[] subjects =
            {
                    "Vegan",
                    "Ovo-Vegetarian",
                    "Lacto-Vegetarian",
                    "Lacto-Ovo Vegetarians",
                    "Pescetarians",
                    "Milk",
                    "Eggs",
                    "Nuts",
                    "Peanuts",
                    "Shellfish",
                    "Wheat",
                    "Soy",
                    "Fish"
            };

    public static final String PREFS_NAME = "PREFS_FILE";

    public String[] getSubjects() {
        return subjects;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        mRecyclerView = findViewById(R.id.recycler_View);

        refreshSharedPreferences();

        mQuizAdapter = new QuizAdapter(subjects, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()); //this is needed for the view of the recycler view, takes care of the scrolling as well
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mQuizAdapter);//responsible for providing views that represent items in the data set

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent quizIntent = new Intent(QuizActivity.this, CustomerLandingPage.class);
                startActivity(quizIntent);
            }
        });
    }

    private void refreshSharedPreferences(){
        // load tasks from preference
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Map<String,String> allEntries = (Map<String, String>) prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.e("map values", entry.getKey() + ": " + entry.getValue().toString());
            //new FetchWeatherData(entry.getKey() + "").execute();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSharedPreferences();
    }

}
