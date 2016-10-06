package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.graphics.Color;
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

        CheckingUtils.changeNotifBarColor("#d35400", getWindow());
        final SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", MODE_PRIVATE);

        //SEND YOUR OWN CHALLENGE
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

                new ServerManager(CreateChallengeManually.this,"SEND_CHALLENGE").execute("SEND_CHALLENGE", body,mail,time_value,title, username,password);



            }
        });


        //ALREADY CREATED CHALLENGES
        final Spinner challenges_spinner = (Spinner) findViewById(R.id.challenges_created);
        final ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(this,
                R.array.challenges, android.R.layout.simple_spinner_item);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        challenges_spinner.setAdapter(gender_adapter);


        final EditText mail_receiver = (EditText) findViewById(R.id.mail_receiver);

        send_challenge = (AppCompatButton) findViewById(R.id.send_challenge);
        send_challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!CheckingUtils.isNetworkConnected(CreateChallengeManually.this)){
                    CheckingUtils.createErrorBox("Norint nusiųsti iššūkį, tau reikia interneto", CreateChallengeManually.this);
                    return;
                }

                String challenge = challenges_spinner.getSelectedItem().toString();
                String mail = mail_receiver.getText().toString();
                String username = sharedPreferences.getString("username", "");
                String password = sharedPreferences.getString("password", "");
                String title =null;
                String time =null;

                if(challenge.equals("Nevartoti alkoholio 7 dienas")){
                    title = "Nevartoju alkoholio";
                    time = "7";
                }

                if(challenge.equals("Gerti daug vandens 21 dieną")){
                    title = "Gerti vandenį";
                    time = "21";
                }

                if(mail.isEmpty()){
                    mail_receiver.setError("Kam nusiųsti?");
                    return;
                }
                new ServerManager(CreateChallengeManually.this,"SEND_CHALLENGE").execute("SEND_CHALLENGE", challenge,mail,time,title, username,password);
            }
        });







    }
}
