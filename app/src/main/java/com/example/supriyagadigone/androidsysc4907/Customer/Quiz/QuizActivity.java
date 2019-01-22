package com.example.supriyagadigone.androidsysc4907.Customer.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.supriyagadigone.androidsysc4907.R;
import com.example.supriyagadigone.androidsysc4907.UserLandingPage;

public class QuizActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "PREFS_FILE";
    private final String TAG = "QuizActivity";

    private RecyclerView mRecyclerView;
    private QuizAdapter mQuizAdapter;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity_grid);

        mRecyclerView = findViewById(R.id.recycler_View);
        mQuizAdapter = new QuizAdapter(RestrictionsData.values(), getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mQuizAdapter);

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent allCust = new Intent(QuizActivity.this, UserLandingPage.class);
                allCust.putExtra("BTN_PRESSED", "0");
                startActivity(allCust);
            }
        });

    }
}
