package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.FacebookSdk;

public class LoginActivity extends AppCompatActivity {

    private EditText username_ET;
    private EditText password_ET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
         
        setContentView(R.layout.activity_login);

        if(!getIntent().getExtras().getBoolean("isAnimDisabled", false))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_out);


        username_ET = (EditText) findViewById(R.id.login_username_text);
        password_ET = (EditText) findViewById(R.id.login_password_text);

    }

    public void logIn(View view){
        String username = username_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();

        if(username.isEmpty()){
            username_ET.setError("Įveskite vartotojo vardą");
            return;
        }
        if(password.isEmpty()){
            password_ET.setError("Įveskite slaptažodį");
            return;
        }

        new ServerManager(this).execute("LOGIN", username,password);

    }


    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
