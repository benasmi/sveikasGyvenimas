package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        TextView textView = (TextView) findViewById(R.id.top);
        TextView textView1 = (TextView) findViewById(R.id.bot_view);


        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/manteka.ttf");

        textView.setTypeface(tf);
        textView1.setTypeface(tf);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(StartingActivity.this, LoginActivity.class));
            }
        }, 2000L);
    }
}
