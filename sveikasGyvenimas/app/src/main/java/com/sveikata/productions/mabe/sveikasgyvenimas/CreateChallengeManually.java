package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateChallengeManually extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge_manually);

        final SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);

        //Time spinner
        final Spinner time_spin = (Spinner)findViewById(R.id.time_spinner);
        ArrayAdapter time_adapter = ArrayAdapter.createFromResource(this,
                R.array.time_limit, R.layout.spinner_item_challenge);
        time_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spin.setAdapter(time_adapter);

        final EditText challenge_mail = (EditText) findViewById(R.id.challenge_mail);
        final EditText challenge_title = (EditText) findViewById(R.id.challenge_title);
        final EditText challenge_body = (EditText) findViewById(R.id.challenge_body);
        AppCompatButton send_challenge = (AppCompatButton) findViewById(R.id.send_challenge_manually);

        send_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!CheckingUtils.isNetworkConnected(CreateChallengeManually.this)){
                    CheckingUtils.createErrorBox("Norint nusiųsti iššūkį, tau reikia interneto", CreateChallengeManually.this);
                    return;
                }

                String mail = challenge_mail.getText().toString();
                String title = challenge_title.getText().toString();
                String body = challenge_body.getText().toString();
                String time_value = time_spin.getSelectedItem().toString();
                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");

                if(title.equals("Laikas")){
                    return;
                }

                new ServerManager(CreateChallengeManually.this,"SEND_CHALLENGE_MANUALLY").execute("SEND_CHALLENGE_MANUALLY", body,mail,time_value,title, username,password);



            }
        });


    }
}
