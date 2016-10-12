package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.google.android.gms.games.Game;

import pl.droidsonroids.gif.GifImageView;

public class YouLostHigherLowerAcitivity extends AppCompatActivity {

    private AppCompatButton try_again;
    private AppCompatButton go_back_to_play_activity;
    private AppCompatButton share_score;

    private TextView score_txt;
    private TextView highscore_txt;
    private GifImageView gif_background;

    private String score;
    private String  highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_lost_higher_lower_acitivity);

        gif_background = (GifImageView) findViewById(R.id.gif_background);

        switch (CheckingUtils.random_int(0, 3)){
            case 0:
                gif_background.setImageResource(R.drawable.fail1);
                break;

            case 1:
                gif_background.setImageResource(R.drawable.fail2);
                break;

            case 2:
                gif_background.setImageResource(R.drawable.fail3);
                break;
        }





        score = getIntent().getExtras().getString("score");
        highscore = getIntent().getExtras().getString("highscore");

        try_again = (AppCompatButton) findViewById(R.id.try_again);
        go_back_to_play_activity = (AppCompatButton) findViewById(R.id.go_back_to_play_bar);
        share_score = (AppCompatButton) findViewById(R.id.share_score);

        score_txt = (TextView) findViewById(R.id.score_game_over);
        highscore_txt = (TextView) findViewById(R.id.highscore_game_over);

        score_txt.setText(score);
        highscore_txt.setText(highscore);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YouLostHigherLowerAcitivity.this, GameScreen.class));
            }
        });

        go_back_to_play_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskQuestionsActivity.addFAQData = true;
                InterestingFactsActivity.addFactsFirstTime = true;
                HealthyLifeActivity.addData = true;
                PlayActivity.shouldAddInfo = true;
                startActivity(new Intent(YouLostHigherLowerAcitivity.this, TabActivityLoader.class).putExtra("Tab", 1));
            }
        });

        share_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckingUtils.shareChallenge("'Aktyvuokis' žaidime surinkau : " + String.valueOf(score), YouLostHigherLowerAcitivity.this, "", "Pabandyk tu, ir pasidalink :P");
            }
        });
    }
}
