package com.example.supriyagadigone.androidsysc4907;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class QuizActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private QuizAdapter mQuizAdapter;
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

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);


        mRecyclerView = findViewById(R.id.recycler_View);

        mQuizAdapter = new QuizAdapter(subjects);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext()); //this is needed for the view of the recycler view, takes care of the scrolling as well
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mQuizAdapter);//responsible for providing views that represent items in the data set
    }
}
