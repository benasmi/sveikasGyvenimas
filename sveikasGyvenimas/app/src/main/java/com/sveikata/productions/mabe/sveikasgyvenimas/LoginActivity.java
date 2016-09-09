package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText username_ET;
    private EditText password_ET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!getIntent().getExtras().getBoolean("isAnimDisabled", false))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_out);

        setContentView(R.layout.activity_login);
        username_ET = (EditText) findViewById(R.id.login_username_text);
        password_ET = (EditText) findViewById(R.id.login_password_text);

    }

    public void logIn(View view){
        String username = username_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();

        //TODO: handle log in
    }


    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

    }
}
