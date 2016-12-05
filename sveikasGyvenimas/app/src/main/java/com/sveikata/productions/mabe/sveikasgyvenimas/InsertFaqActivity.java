package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InsertFaqActivity extends AppCompatActivity {


    private EditText faq_title;
    private EditText faq_body;
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_faq);

        faq_title = (EditText) findViewById(R.id.faq_title);
        faq_body = (EditText) findViewById(R.id.faq_body);

        sharedPrefs = getSharedPreferences("DataPrefs", MODE_PRIVATE);
    }


    public void insertFaq(View view) {

        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Reikalingas interneto ryšys" ,this, R.style.CasualStyle);
            return;
        }

        String faq_title_value = faq_title.getText().toString();
        String faq_body_value = faq_body.getText().toString();
        String username = sharedPrefs.getString("username", "");
        String password = sharedPrefs.getString("password", "");

        if(faq_title_value.isEmpty()){
            faq_title.setError("Šis laukelis negali buti tušcias");
            return;
        }

        if(faq_body_value.isEmpty()){
            faq_body.setError("Šis laukelis negali buti tušcias");
            return;
        }

        new ServerManager(this, "INSERT_FAQ").execute("INSERT_FAQ", faq_title_value, faq_body_value, username, password);
    }
}
