package com.example.supriyagadigone.androidsysc4907;

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.QuizActivity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuizActivityTest {
    @Test
    public void numberOfItemsInRestriction(){
        QuizActivity quizActivity = new QuizActivity();
        assertEquals(13, quizActivity.getSubjects().length);
    }
}
