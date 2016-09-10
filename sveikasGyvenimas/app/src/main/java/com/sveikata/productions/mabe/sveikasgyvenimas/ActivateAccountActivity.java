package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ActivateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);

        TextView editText = (TextView) findViewById(R.id.textView3);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/manteka.ttf");
        editText.setTypeface(tf);

    }
}
