package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuizActivity extends AppCompatActivity {

    private TextView question_textview;
    private TextView timer;
    private TextView score_textview;
    private CountDownTimer countDownTimer;
    private AppCompatButton answer1;
    private AppCompatButton answer2;
    private AppCompatButton answer3;
    private AppCompatButton answer4;

    private boolean canClick = true;
    private SharedPreferences sharedPreferences;
    public static final int questionCount = 5;

    public static final String[] question_1 = {"Kokia yra mirtina alkoholio dozė?", "dahuja", "dahuja su biski", "labai dahuja", "labai dahuja su biski", "labai dahuja su biski"};
    public static final String[] question_2 = {"Kas tu??", "Nu nedaug", "Nu biski", "Px man", "Daug", "Daug"};
    public static final String[] question_3 = {"Kodėl taip sakai?", "Nu nedaug", "Nu biski", "Px man", "Daug", "Daug"};
    public static final String[] question_4 = {"Kaip kepti blynus?", "Nu nedaug", "Nu biski", "Px man", "Daug", "Daug"};
    public static final String[] question_5 = {"Ar astunkojis yra kebabo lekste?", "Nu nedaug", "Nu biski", "Px man", "Daug", "Daug"};

    public static int question;
    public static int score;
    private int highscore;

    int correct_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        highscore = sharedPreferences.getInt("highscore_quiz", 0);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        CheckingUtils.changeNotifBarColor("#2B3C50", getWindow());


        generatePolnijBred();


        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(QuizActivity.this, QuizActivity.class ).putExtra("Tab", 1));
            }
        };


        View.OnClickListener wrongListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(canClick){

                countDownTimer.cancel();
                question++;
                canClick=false;




                switch (correct_answer){
                    case 0:
                        answer1.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 1:
                        answer2.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 2:
                        answer3.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                    case 3:
                        answer4.setBackgroundColor(Color.parseColor("#27ae60"));
                        v.setBackgroundColor(Color.parseColor("#e74c3c"));
                        break;

                }

                CheckingUtils.vibrate(QuizActivity.this, 500);
                new Timer().schedule(task, 1500);
            }
            }

        };
        View.OnClickListener rightListener = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(canClick){


                canClick=false;
                question++;
                countDownTimer.cancel();
                v.setBackgroundColor(Color.parseColor("#27ae60"));
                new Timer().schedule(task, 1500);
                score++;
                score_textview.setText(score + "/" + question);
            }
            }
        };

        answer1.setOnClickListener(wrongListener);
        answer2.setOnClickListener(wrongListener);
        answer3.setOnClickListener(wrongListener);
        answer4.setOnClickListener(wrongListener);

        timer = (TextView) findViewById(R.id.quiz_timer);
        question_textview = (TextView) findViewById(R.id.quiz_question);
        score_textview = (TextView) findViewById(R.id.quiz_score);
        score_textview.setText(score + "/" + question);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");
        question_textview.setTypeface(tf);
        timer.setTypeface(tf);

        String question_title = null;
        String question_answer_1 = null;
        String question_answer_2 = null;
        String question_answer_3 = null;
        String question_answer_4 = null;
        String rightAnswer = null;

        switch (question){
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

            case 2:

                question_title = question_3[0];
                question_answer_1 = question_3[1];
                question_answer_2 = question_3[2];
                question_answer_3 = question_3[3];
                question_answer_4 = question_3[4];
                rightAnswer = question_3[5];


                break;

            case 3:

                question_title = question_4[0];
                question_answer_1 = question_4[1];
                question_answer_2 = question_4[2];
                question_answer_3 = question_4[3];
                question_answer_4 = question_4[4];
                rightAnswer = question_4[5];
                break;

            case 4:

                question_title = question_5[0];
                question_answer_1 = question_5[1];
                question_answer_2 = question_5[2];
                question_answer_3 = question_5[3];
                question_answer_4 = question_5[4];
                rightAnswer = question_5[5];
                break;

            case 5:
                if(score>highscore){
                    sharedPreferences.edit().putInt("highscore_quiz", score).commit();
                }
                startActivity(new Intent(QuizActivity.this, YouLostQuizActivity.class).putExtra("score", score).putExtra("highscore_quiz", highscore));
                break;
        }

        question_textview.setText(question_title);

        answer1.setText(question_answer_1);
        answer2.setText(question_answer_2);
        answer3.setText(question_answer_3);
        answer4.setText(question_answer_4);

        if(answer1.getText().toString().equals(rightAnswer)){
            answer1.setOnClickListener(rightListener);
            correct_answer = 0;
        }
        if(answer2.getText().toString().equals(rightAnswer)){
            answer2.setOnClickListener(rightListener);
            correct_answer = 1;
        }
        if(answer3.getText().toString().equals(rightAnswer)){
            answer3.setOnClickListener(rightListener);
            correct_answer = 2;
        }
        if(answer4.getText().toString().equals(rightAnswer)){
            answer4.setOnClickListener(rightListener);
            correct_answer = 3;
        }

        question_textview.setText(question_title);

        if(question<=5){

       countDownTimer = new CountDownTimer(16000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                canClick = false;
                try{

                    timer.setText(String.valueOf(0));

                    switch (correct_answer){
                        case 0:
                            answer1.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 1:
                            answer2.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 2:
                            answer3.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;

                        case 3:
                            answer4.setBackgroundColor(Color.parseColor("#27ae60"));
                            break;
                    }

                    CheckingUtils.vibrate(QuizActivity.this, 500);
                    new Timer().schedule(task, 1500);

                }catch (Exception e){
                }

            }
        }.start();

    }
    }
    private int random_int(int min, int max)
    {
        return (int) (Math.random()*(max-min))+min;
    }

    private void generatePolnijBred(){
        int randomButtonQuestions = random_int(0,4);
        Log.i("TEST", String.valueOf(randomButtonQuestions));
        switch (randomButtonQuestions){
            case 0:
                answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
                answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
                answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
                answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_4);
                break;
            case 1:
                answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
                answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
                answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
                answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_4);
                break;

            case 2:
                answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
                answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
                answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
                answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_4);
                break;

            case 3:
                answer1 = (AppCompatButton) findViewById(R.id.quiz_answer_4);
                answer2 = (AppCompatButton) findViewById(R.id.quiz_answer_3);
                answer3 = (AppCompatButton) findViewById(R.id.quiz_answer_2);
                answer4 = (AppCompatButton) findViewById(R.id.quiz_answer_1);
                break;
        }
    }
}