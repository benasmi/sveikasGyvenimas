package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class AskQuestionsActivity extends android.support.v4.app.Fragment{


    public static boolean addFAQData = true;

    private ArrayList<QuestionsDataHolder> data = new ArrayList<QuestionsDataHolder>();
    public static RecyclerView recyclerView;
    private RecyclerAdapterQuestions adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;
    private String is_administrator;
    private boolean show = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.activity_ask_questions,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.questions_recycler);
        adapter = new RecyclerAdapterQuestions(getActivity(),data);

        SharedPreferences userPrefs = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);

        try {
            JSONArray user_data_array = new JSONArray(userPrefs.getString("user_data", ""));
            JSONObject user_data = user_data_array.getJSONObject(0);
            is_administrator = user_data.getString("is_admin");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(addFAQData){
            addFAQData=false;
            initializeData(adapter);
            initializeDataFirstTime(adapter);

        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }



    public void initializeData(RecyclerAdapterQuestions adapter){


//        adapter.add(new QuestionsDataHolder("Kuriose savivaldybėse vyks projekto renginiai?", "Preliminariai Rietavo r., Kupiškio r., Molėtų r., Biržų r., Anykščių r., Kalvarijos, Prienų r., Šalčininkų r., Trakų r., Pasvalio r., Pagėgių r., Kelmės r., Širvintų r., Rokiškio r., Ignalinos r., Ukmergės r., Zarasų r., Alytaus r., Panevėžio r., Kaišiadorių r., Varėnos r., Lazdijų r., Vilkaviškio r., Kėdainių r., Telšių r., Šilalės r., Raseinių r., Švenčionių r., Jurbarko r., Kretingos r.",2), 2);
//        adapter.add(new QuestionsDataHolder("Kas yra Sveikos gyvensenos ambasadorius?", "Sveikos gyvensenos ambasadorius – tai asmuo, kuris rūpinsis projekto renginių ir konsultacijų organizavimu bei padės kiekvienam Projekto dalyviui suvokti savo asmenines galimybes, padės išlikti ryžtingam ugdant sveikos gyvensenos įpročius. ",2), 2);
//        adapter.add(new QuestionsDataHolder("Kaip galėčiau sužinoti apie mano mieste vykstančius renginius?", "Daugiau apie renginius tavo mieste žiūrėti 'Tvarkaraštis' skiltyje.",2),2);
//        adapter.add(new QuestionsDataHolder("Kaip galėčiau prisidėti prie projekto įgyvendinimo?", "Tapk projekto socialiniu partneriu, tapk projekto dalyviu. Parašyk mums: info@dvp.lt ir tap mūsų komandos nariu.",2), 2);
//        adapter.add(new QuestionsDataHolder("Ko siekiama projektu?", "Vykdyti alkoholio vartojimo prevenciją tarp vaikų ir jaunimo, įtraukiant jų tėvus (teisėtus vaiko atstovus pagal įstatymą), ugdymo įstaigas ir kitus bendruomenės narius.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kam skirtas šis projektas?", "Vaikams ir jaunimui, tėvams, vietos bendruomenių nariams, kitoms gyventojų grupėms.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kas finansuoja projektą?", "Projektas finansuojamas Valstybinio visuomenės sveikatos stiprinimo fondo lėšomis.",0), 2);
//        adapter.add(new QuestionsDataHolder("Projekto įgyvendinimo trukmė?", "Tai 12 mėn. projektas.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais..",0), 2);
//        adapter.add(new QuestionsDataHolder("Koks projekto tikslas?", "Skatinti vaikus, jaunimą, kitas gyventojų grupes atsisakyti alkoholio ir sveikiau gyventi.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kas vyks šio projekto metu?", "•\tBus sukurtas puslapis socialiniame tinkle „Facebook“ ir nemokama interaktyvi Android mobili aplikacija apie sveikatą, sveiką gyvenseną, alkoholio daromą žalą. \n•\tProjekte dalyvausiančiose savivaldybėse bus parengti Sveikos gyvensenos ambasadoriai, kurie padės vykdyti alkoholio prevencijos bei sveikos gyvensenos renginius savivaldybėse, kitas projekto veiklas. \n•\tProjekte dalyvausiančiose savivaldybėse (mokyklose, atviruose jaunimo centruose, masiniuose renginiuose ir kitur) bus vykdomi alkoholio prevencijos per sveikos gyvensenos prizmę renginiai.\n•\tProjekte dalyvausiančiose savivaldybėse bus vykdomas marškinėlių maketavimo konkursas „Sveikas gyvenimas – blaivus gyvenimas!“.",0), 2);
//        adapter.add(new QuestionsDataHolder("Kas įgyvendina projektą?", "Projektą įgyvendina Viešoji įstaiga „Darnaus vystymo projektai“ kartu su partneriais..",0), 2);

        SharedPreferences loginPrefs = getActivity().getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

        try {
            JSONArray faqData = new JSONArray(loginPrefs.getString("faq_data", "{}"));

            for(int i = 0; i < faqData.length(); i++){
                JSONObject obj = faqData.getJSONObject(i);
                String title = obj.getString("faq_title");
                String body = obj.getString("faq_body");


                adapter.add(new QuestionsDataHolder(title,body, 2), 0);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void initializeDataFirstTime(RecyclerAdapterQuestions adapter){
        adapter.add(new QuestionsDataHolder("","",1),0);
        adapter.add(new QuestionsDataHolder("", "", 3), 0);
        if(is_administrator.equals("1")){
            adapter.add(new QuestionsDataHolder("","",4),0);
        }
    }



}
