package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView question_textview;
    private TextView timer;

    private AppCompatButton answer1;
    private AppCompatButton answer2;
    private AppCompatButton answer3;
    private AppCompatButton answer4;


    public static final String[] question_1 = {"Kokia yra mirtina alkoholio dozė?", "dahuja", "dahuja su biski", "labai dahuja", "labai dahuja su biski", "labai dahuja su biski"};
    public static final String[] question_2 = {"Kiek viena cigaretė sutrumpina gyvenimo trukmę?", "Nu nedaug", "Nu biski", "Px man", "Daug", "Daug"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
        answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
        answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
        answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_4);

        View.OnClickListener wrongListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckingUtils.createErrorBox("Atsakymas neteisingas!", QuizActivity.this);
            }
        };
        View.OnClickListener rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizActivity.this, PlayActivity.class));
            }
        };

        answer1.setOnClickListener(wrongListener);
        answer2.setOnClickListener(wrongListener);
        answer3.setOnClickListener(wrongListener);
        answer4.setOnClickListener(wrongListener);

        timer = (TextView) findViewById(R.id.quiz_timer);
        question_textview = (TextView) findViewById(R.id.quiz_question);

        int question_count = 2; //CHANGE THIS ACCORDING TO QUESTION COUNT

        String question_title = null;
        String question_answer_1 = null;
        String question_answer_2 = null;
        String question_answer_3 = null;
        String question_answer_4 = null;
        String rightAnswer = null;

        switch (random_int(0, question_count-1)){
            case 0:

                question_title = question_1[0];
                question_answer_1 = question_1[1];
                question_answer_2 = question_1[2];
                question_answer_3 = question_1[3];
                question_answer_4 = question_1[4];
                rightAnswer = question_1[5];




                break;
            case 1:

                question_title = question_2[0];
                question_answer_1 = question_2[1];
                question_answer_2 = question_2[2];
                question_answer_3 = question_2[3];
                question_answer_4 = question_2[4];
                rightAnswer = question_2[5];


                break;
        }

        question_textview.setText(question_title);

        answer1.setText(question_answer_1);
        answer2.setText(question_answer_2);
        answer3.setText(question_answer_3);
        answer4.setText(question_answer_4);

        if(answer1.getText().toString().equals(rightAnswer)){
            answer1.setOnClickListener(rightListener);
        }
        if(answer2.getText().toString().equals(rightAnswer)){
            answer2.setOnClickListener(rightListener);
        }
        if(answer3.getText().toString().equals(rightAnswer)){
            answer3.setOnClickListener(rightListener);
        }
        if(answer4.getText().toString().equals(rightAnswer)){
            answer4.setOnClickListener(rightListener);
        }


        question_textview.setText(question_title);




    }

    private int random_int(int min, int max)
    {
        return (int) (Math.random()*(max-min))+min;
    }
    
}
