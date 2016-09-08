package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText password;
    private EditText repeat_password;
    private EditText mail;
    private EditText username;
    private Spinner years;
    private Spinner gender;


    //Entered value
    private String name_value;
    private String username_value;
    private String password_value;
    private String repeat_password_value;
    private String years_value;
    private String gender_value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //EditTexts
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        password = (EditText) findViewById(R.id.password);
        repeat_password = (EditText) findViewById(R.id.repeat_password);
        mail = (EditText) findViewById(R.id.mail);
        username = (EditText) findViewById(R.id.username);

        //Spinners
        years = (Spinner) findViewById(R.id.years);
        ArrayAdapter<CharSequence> years_adapter = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(years_adapter);



        gender = (Spinner) findViewById(R.id.gender);
        final ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);



    }



    public void register(View view) {

    }
}
