package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText name_ET;
    private EditText last_name_ET;
    private EditText password_ET;
    private EditText repeat_password_ET;
    private EditText mail_ET;
    private EditText username_ET;
    private Spinner years;
    private Spinner gender;

    //Entered value
    private String name_value;
    private String last_name_value;
    private String mail_value;
    private String username_value;
    private String password_value;
    private String repeat_password_value;
    private String years_value;
    private String gender_value;

    private TextView register_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //EditTexts
        name_ET = (EditText) findViewById(R.id.name);
        last_name_ET = (EditText) findViewById(R.id.last_name);
        password_ET = (EditText) findViewById(R.id.password);
        repeat_password_ET = (EditText) findViewById(R.id.repeat_password);
        mail_ET = (EditText) findViewById(R.id.mail);
        username_ET = (EditText) findViewById(R.id.username);


        register_txt = (TextView) findViewById(R.id.register_textview);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/bevan.ttf");
        register_txt.setTypeface(tf);


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
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        years_value = years.getSelectedItem().toString();
        gender_value = gender.getSelectedItem().toString();
        name_value = name_ET.getText().toString();
        last_name_value = last_name_ET.getText().toString();
        username_value = username_ET.getText().toString();
        password_value = password_ET.getText().toString();
        repeat_password_value = repeat_password_ET.getText().toString();
        mail_value = mail_ET.getText().toString();

        if(username_value.isEmpty()){

            username_ET.setError("Prašome įvesti vartotojo vardą");
            return;
        }
        if(name_value.isEmpty()){
            name_ET.setError("Įveskite savo vardą");
            return;
        }
        if(last_name_value.isEmpty()){
            last_name_ET.setError("Įveskite savo pavardę");
            return;
        }
        if(mail_value.isEmpty() || !mail_value.contains("@")){
            mail_ET.setError("Įveskite galiojantį pašto adresą");
            return;
        }
        if(password_value.isEmpty()){
            password_ET.setError("Įveskite slaptažodį");
            return;
        }
        if(!password_value.equals(repeat_password_value)){
            password_ET.setError("Slaptažodžiai nesutampa");
            repeat_password_ET.setError("Slaptažodžiai nesutampa");
            return;
        }
        if(years_value.equals("Metai")){
            TextView errorText = (TextView) years.getSelectedView();
            errorText.setError("Pasirinkite metus");
            errorText.startAnimation(shake);
            return;

        }
        if(gender_value.equals("Lytis")) {
            TextView errorText = (TextView) gender.getSelectedView();
            errorText.setError("Pasirinkite lytį");
            errorText.startAnimation(shake);
            return;
        }

        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Norėdami prisijungti, turite įjungti internetą",this);
            return;
        }
        new ServerManager(this).execute("REGISTRATION",name_value,last_name_value,username_value,password_value,mail_value,gender_value,years_value,"regular");

    }
}
