package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class AskProfessionalActivity extends AppCompatActivity {

    //Button for user
    private AppCompatButton ask_button;
    private EditText message;
    private Spinner spinner;
    private String sender;
    private Typeface tf;
    private TextView txt;
    private SharedPreferences sharedPreferences;
    private JSONArray jsonArray;
    private JSONObject userData;
    public static String specialist = "Sveikatos specialistė";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_professional);
        CheckingUtils.changeNotifBarColor("#27382e", getWindow());
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String user_data = sharedPreferences.getString("user_data", "");

        try{
            jsonArray = new JSONArray(user_data);
            userData = jsonArray.getJSONObject(0);
            sender = userData.getString("mail");
        }catch (Exception e){

        }







        tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");

        //Setting up spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.specialists, R.layout.spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        //Sending email
        message = (EditText) findViewById(R.id.email_message);
        txt = (TextView) findViewById(R.id.ask_new_question_txt);
        ask_button = (AppCompatButton) findViewById(R.id.ask);
        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_value = message.getText().toString().trim();
                String specialist = spinner.getSelectedItem().toString();

                if(specialist.equals("Pasirink specialistą")){
                    CheckingUtils.createErrorBox("Pasirinkimas negalimas", AskProfessionalActivity.this, R.style.QuestionsDialogStyle);
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }
                if(message_value.equals(null)){
                    message.setError("Paklausk ko nors !)");
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }

                if(!CheckingUtils.isNetworkConnected(AskProfessionalActivity.this)){
                    CheckingUtils.createErrorBox("Norint parašyti specialistui, tau reikia interneto :?", AskProfessionalActivity.this,R.style.QuestionsDialogStyle);
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }

                send_mail(specialist, message_value);


            }
        });

    }

    public void send_mail(final String subject, final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Pabaigei pildyti savo klausimą :?")
                .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ServerManager(AskProfessionalActivity.this, "SEND_MAIL").execute("SEND_MAIL",subject, message, sender);

                    }
                })
                .setNegativeButton("DAR PATAISYSIU KLAUSIMĄ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
        builder.create();
    }

}
