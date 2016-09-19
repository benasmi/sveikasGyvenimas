package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AskQuestionsActivity extends android.support.v4.app.Fragment{

    public static String specialist_BEN = "Benas. M";
    public static String specialist_MARTIN = "Martynas. D";
    public static boolean addFAQData = true;

    private ArrayList<QuestionsDataHolder> data = new ArrayList<QuestionsDataHolder>();
    private RecyclerView recyclerView;
    private RecyclerAdapterQuestions adapter;
    private AppCompatButton ask_button;
    private EditText message;
    private EditText subject;
    private Spinner spinner;




    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;
    private TextView faq_txt;
    private TextView ask_question_txt;
    private String is_administrator;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Preferences to check if user is admin
        SharedPreferences userPrefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
        String user_data = userPrefs.getString("user_data", "");



        try {
            jsonArray = new JSONArray(user_data);
            userData = jsonArray.getJSONObject(0);


            is_administrator = userData.getString("is_admin");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        View rootView = inflater.inflate(R.layout.activity_ask_questions,container,false);

        //TEXTVIEWS
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/comforta.ttf");
        faq_txt = (TextView) rootView.findViewById(R.id.faq_txt);
        ask_question_txt = (TextView) rootView.findViewById(R.id.ask_new_question_txt);
        faq_txt.setTypeface(tf);
        ask_question_txt.setTypeface(tf);

        //Setting up spinner
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.specialists, R.layout.spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);


        //Sending email
        message = (EditText) rootView.findViewById(R.id.email_message);
        subject = (EditText) rootView.findViewById(R.id.subject_email);
        ask_button = (AppCompatButton) rootView.findViewById(R.id.ask);

        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_value = message.getText().toString().trim();
                String subject_value = subject.getText().toString().trim();

                String specialist = spinner.getSelectedItem().toString();

                if(specialist.equals("Pasirink specialistą")){
                    CheckingUtils.createErrorBox("Pasirinkimas negalimas", getActivity());
                    return;
                }
                else if(specialist.equals(specialist_BEN)){
                    String to [] = {"benasmiliunas@gmail.com"};
                    sendEmail(to,subject_value,message_value);
                }else if(specialist.equals(specialist_MARTIN)){
                    String to [] = {"dargis.martynas@gmail.com"};
                    sendEmail(to,subject_value,message_value);
                }

            }
        });


        recyclerView = (RecyclerView) rootView.findViewById(R.id.questions_recycler);
        adapter = new RecyclerAdapterQuestions(getActivity(),data);

        if(addFAQData){
            addFAQData=false;
            initializeData(adapter);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }



    public void initializeData(RecyclerAdapterQuestions adapter){
        //Preferences to fetch all schedule data
        SharedPreferences dataPrefs = getActivity().getSharedPreferences("QuestionsData", getActivity().MODE_PRIVATE);
        String schedule = dataPrefs.getString("questions_data", "");

        adapter.add(new QuestionsDataHolder("Ko siekiama projektu?", "Vykdyti alkoholio vartojimo prevenciją tarp vaikų ir jaunimo, įtraukiant jų tėvus (teisėtus vaiko atstovus pagal įstatymą), ugdymo įstaigas ir kitus bendruomenės narius."), 0);
        adapter.add(new QuestionsDataHolder("Kam skirtas šis projektas?", "Vaikams ir jaunimui, tėvams, vietos bendruomenių nariams, kitoms gyventojų grupėms."), 0);
        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais."), 0);
        adapter.add(new QuestionsDataHolder("Kas finansuoja projektą?", "Projektas finansuojamas Valstybinio visuomenės sveikatos stiprinimo fondo lėšomis."), 0);
        adapter.add(new QuestionsDataHolder("Kuriose savivaldybėse vyks projekto renginiai?", "Preliminariai Rietavo r., Kupiškio r., Molėtų r., Biržų r., Anykščių r., Kalvarijos, Prienų r., Šalčininkų r., Trakų r., Pasvalio r., Pagėgių r., Kelmės r., Širvintų r., Rokiškio r., Ignalinos r., Ukmergės r., Zarasų r., Alytaus r., Panevėžio r., Kaišiadorių r., Varėnos r., Lazdijų r., Vilkaviškio r., Kėdainių r., Telšių r., Šilalės r., Raseinių r., Švenčionių r., Jurbarko r., Kretingos r."), 0);
        adapter.add(new QuestionsDataHolder("Projekto įgyvendinimo trukmė?", "Tai 12 mėn. projektas."), 0);
        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais.."), 0);
        adapter.add(new QuestionsDataHolder("Koks projekto tikslas?", "Skatinti vaikus, jaunimą, kitas gyventojų grupes atsisakyti alkoholio ir sveikiau gyventi."), 0);
        adapter.add(new QuestionsDataHolder("Kas vyks šio projekto metu?", "•\tBus sukurtas puslapis socialiniame tinkle „Facebook“ ir nemokama interaktyvi Android mobili aplikacija apie sveikatą, sveiką gyvenseną, alkoholio daromą žalą. \n•\tProjekte dalyvausiančiose savivaldybėse bus parengti Sveikos gyvensenos ambasadoriai, kurie padės vykdyti alkoholio prevencijos bei sveikos gyvensenos renginius savivaldybėse, kitas projekto veiklas. \n•\tProjekte dalyvausiančiose savivaldybėse (mokyklose, atviruose jaunimo centruose, masiniuose renginiuose ir kitur) bus vykdomi alkoholio prevencijos per sveikos gyvensenos prizmę renginiai.\n•\tProjekte dalyvausiančiose savivaldybėse bus vykdomas marškinėlių maketavimo konkursas „Sveikas gyvenimas – blaivus gyvenimas!“."), 0);
        adapter.add(new QuestionsDataHolder("Kas yra Sveikos gyvensenos ambasadorius?", "Sveikos gyvensenos ambasadorius – tai asmuo, kuris rūpinsis projekto renginių ir konsultacijų organizavimu bei padės kiekvienam Projekto dalyviui suvokti savo asmenines galimybes, padės išlikti ryžtingam ugdant sveikos gyvensenos įpročius. "), 0);
        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais.."), 0);
        adapter.add(new QuestionsDataHolder("Kaip galėčiau sužinoti apie mano mieste vykstančius renginius?", "Daugiau apie renginius tavo mieste žiūrėti 'Tvarkaraštis' skiltyje."), 0);
        adapter.add(new QuestionsDataHolder("Kaip galėčiau prisidėti prie projekto įgyvendinimo?", "Tapk projekto socialiniu partneriu, tapk projekto dalyviu. Parašyk mums: info@dvp.lt ir tap mūsų komandos nariu."), 0);

    }
    public void sendEmail(String[] to, String subject, String message){



        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");

        Intent email = Intent.createChooser(intent,"Email");
        getActivity().startActivity(email);

    }


}
