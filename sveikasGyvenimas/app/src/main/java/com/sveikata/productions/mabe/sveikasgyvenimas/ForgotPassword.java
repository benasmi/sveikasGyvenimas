package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPassword extends AppCompatActivity {



    private TextView text;
    private TextView change_pass_txt;

    private EditText mail;
    private EditText sent_code;
    private EditText new_pass;
    private EditText repeat_new_pass;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String mail_value;
    private String sent_code_value;
    private String new_pass_value;
    private String repeat_new_pass_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");

        sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Texts
        text = (TextView) findViewById(R.id.forgot_pass_textview);
        change_pass_txt = (TextView) findViewById(R.id.textView13);
        change_pass_txt.setTypeface(tf);

        //Edittexts
        mail = (EditText) findViewById(R.id.forgot_password_mail);
        sent_code = (EditText) findViewById(R.id.editText2);
        new_pass = (EditText) findViewById(R.id.forgot_password_new);
        repeat_new_pass = (EditText) findViewById(R.id.forgot_password_repeat);


    }


    public void send_activation_code(View view) {
        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Šiam veiksmui atlikti reikalingas internetas!", this, R.style.ScheduleDialogStyle);
            return;
        }

        mail_value = mail.getText().toString();
        if(mail_value.isEmpty()){
            mail.setError("Įveskite paštą");
            return;
        }


        int number = CheckingUtils.random_int(1000, 9999);
        editor.putInt("change_password_code", number).commit();

        new ServerManager(this, "SEND_CHANGE_PASSWORD_CODE").execute("SEND_CHANGE_PASSWORD_CODE", mail_value, String.valueOf(number));
    }

    public void change_password(View view) {
        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Šiam veiksmui atlikti reikalingas internetas!", this, R.style.ScheduleDialogStyle);
            return;
        }

        mail_value = mail.getText().toString();
        sent_code_value = sent_code.getText().toString();
        new_pass_value = new_pass.getText().toString();
        repeat_new_pass_value = repeat_new_pass.getText().toString();


        if(sent_code_value.isEmpty()){
            sent_code.setError("Įveskite kodą, kurį jums atsiuntėme");
            return;
        }


        if(new_pass_value.isEmpty()){
            new_pass.setError("Įveskite naują slaptažodį");
            return;
        }
        if(sent_code_value.length()<4){
            sent_code.setError("Kodas turi būti nuo 1000 - 9999");
        }

        if(!repeat_new_pass_value.equals(new_pass_value)){
            repeat_new_pass.setError("Slaptažodžiai nesutampa!");
            return;
        }
        if(!sent_code_value.equals(String.valueOf(sharedPreferences.getInt("change_password_code", 0)))){
            Log.i("TEST",String.valueOf(sharedPreferences.getInt("change_password_code", 0) + ", " + sent_code_value));
            CheckingUtils.createErrorBox("Kodas nesutampa, su kodu, kuris buvo nusiųstas į jūsų paštą", this, R.style.CasualStyle);
            return;
        }


        new ServerManager(this, "CHANGE_PASSWORD").execute("CHANGE_PASSWORD", sent_code_value, new_pass_value);


    }
}
