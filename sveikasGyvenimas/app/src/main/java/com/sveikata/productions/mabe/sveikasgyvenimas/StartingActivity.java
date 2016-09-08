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


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(StartingActivity.this, LoginActivity.class));
            }
        }, 2000L);
    }
}
