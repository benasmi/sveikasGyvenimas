package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class YouLostQuizActivity extends AppCompatActivity {

    private AppCompatButton try_again;
    private AppCompatButton go_back_to_play_activity;
    private AppCompatButton share_score;
    private RatingBar ratingBar;

    private Typeface tf;
    private Typeface tfBevan;

    private TextView score_txt;
    private TextView highscore_txt;
    private TextView percentage_txt;
    private GifImageView gif_background;


    private int score;
    private int maxQuestions=12;
    private int  highscore;
    private int percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        QuizActivity.score = 0;
        QuizActivity.question = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_lost_quiz);

        tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");
        tfBevan = Typeface.createFromAsset(getAssets(), "fonts/bevan.ttf");

        gif_background = (GifImageView) findViewById(R.id.gif_background_quiz);
        gif_background.setImageResource(R.drawable.quiz_over);

        score = getIntent().getExtras().getInt("score");
        highscore = getIntent().getExtras().getInt("highscore_quiz");


        percentage = (100*score)/maxQuestions;
        Log.i("TEST", String.valueOf(percentage));

        try_again = (AppCompatButton) findViewById(R.id.try_again_quiz);
        go_back_to_play_activity = (AppCompatButton) findViewById(R.id.go_back_to_play_bar_quiz);
        share_score = (AppCompatButton) findViewById(R.id.share_score_quiz);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        ratingBar.setNumStars(5);
        ratingBar.setEnabled(false);
        percentage_txt = (TextView)findViewById(R.id.percentage_txt);

        score_txt = (TextView) findViewById(R.id.score_game_over_quiz);
        highscore_txt = (TextView) findViewById(R.id.highscore_game_over_quiz);

        score_txt.setText(score+ "/" + "31");
        highscore_txt.setText(highscore+ "/" + "31");

        percentage_txt.setTypeface(tfBevan);
        score_txt.setTypeface(tf);
        highscore_txt.setTypeface(tf);

        if(percentage>=0 && percentage<=20){
            ratingBar.setRating(1);
            percentage_txt.setText("Nekaip, reikėtų pasistengti :/");

        }
        if(percentage>=21 && percentage<=40){
            ratingBar.setRating(2);
            percentage_txt.setText("Galėtum geriau...");
            ;
        }
        if(percentage>=41 && percentage<=70){
            ratingBar.setRating(3);
            percentage_txt.setText("Bandyk dar kartą");

        }
        if(percentage>=71 && percentage<=94){
            ratingBar.setRating(4);
            percentage_txt.setText("Beveik tobula!");

        }
        if(percentage>=95 && percentage<=100){
            ratingBar.setRating(5);
            percentage_txt.setText("Šaunu!!!");

        }



        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YouLostQuizActivity.this, QuizActivity.class));
            }
        });

        go_back_to_play_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskQuestionsActivity.addFAQData = true;
                InterestingFactsActivity.addFactsFirstTime = true;
                HealthyLifeActivity.addData = true;
                PlayActivity.shouldAddInfo = true;
                startActivity(new Intent(YouLostQuizActivity.this, TabActivityLoader.class).putExtra("Tab", 1));
            }
        });

        share_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckingUtils.shareChallenge("'Aktyvuokis' žaidime surinkau : " + String.valueOf(score), YouLostQuizActivity.this, "", "Pabandyk tu, ir pasidalink :P");
            }
        });
    }
}
