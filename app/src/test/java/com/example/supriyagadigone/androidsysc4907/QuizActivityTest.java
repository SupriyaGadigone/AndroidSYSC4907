package com.example.supriyagadigone.androidsysc4907;

import com.example.supriyagadigone.androidsysc4907.Customer.Quiz.RestrictionsData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuizActivityTest {
    @Test
    public void numberOfItemsInRestriction(){
        assertEquals(13, RestrictionsData.values().length);
    }
}
