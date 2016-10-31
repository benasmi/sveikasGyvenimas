package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SuicideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suicide);
        CheckingUtils.changeNotifBarColor("#2b2b2b", getWindow());
    }

    private void sendToCallIntent(String phone_numbah){
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_numbah));
        startActivity(i);
    }

    public void skambintiVaikuLinijai(View view) {
        sendToCallIntent("123456");
    }
}
