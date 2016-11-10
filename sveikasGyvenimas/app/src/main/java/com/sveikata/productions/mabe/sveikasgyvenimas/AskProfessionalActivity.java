package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AskProfessionalActivity extends AppCompatActivity {

    //Button for user
    private AppCompatButton ask_button;
    private EditText message;
    private EditText subject;
    private Spinner spinner;
    private Typeface tf;
    private TextView txt;
    public static String specialist = "Sveikatos specialistė";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_professional);
        CheckingUtils.changeNotifBarColor("#27382e", getWindow());

        tf = Typeface.createFromAsset(getAssets(),"fonts/Verdana.ttf");

        //Setting up spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.specialists, R.layout.spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);

        //Sending email
        message = (EditText) findViewById(R.id.email_message);
        subject = (EditText) findViewById(R.id.subject_email);
        txt = (TextView) findViewById(R.id.ask_new_question_txt);
        ask_button = (AppCompatButton) findViewById(R.id.ask);
        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_value = message.getText().toString().trim();
                String subject_value = subject.getText().toString().trim();
                String specialist = spinner.getSelectedItem().toString();

                if(specialist.equals("Pasirink specialistą")){
                    CheckingUtils.createErrorBox("Pasirinkimas negalimas", AskProfessionalActivity.this, R.style.QuestionsDialogStyle);
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }
                if(message_value.equals(null)){
                    message.setError("Paklausk ko nors :D!");
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }

                if(!CheckingUtils.isNetworkConnected(AskProfessionalActivity.this)){
                    CheckingUtils.createErrorBox("Norint parašyti specialistui, tau reikia interneto :?", AskProfessionalActivity.this,R.style.QuestionsDialogStyle);
                    CheckingUtils.vibrate(AskProfessionalActivity.this, 100);
                    return;
                }

                else if(specialist.equals(specialist)){
                    String to [] = {"blaivusgyvenimas@gmail.com"};
                    sendEmail(to,subject_value,message_value);
                }

            }
        });

    }

    public void sendEmail(String[] to, String subject, String message)
    {

        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");

        Intent email = Intent.createChooser(intent,"Email");
        this.startActivity(email);

    }

}
