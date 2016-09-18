package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AskQuestionsActivity extends android.support.v4.app.Fragment{

    private ArrayList<QuestionsDataHolder> data = new ArrayList<QuestionsDataHolder>();
    private RecyclerView recyclerView;
    private RecyclerAdapterQuestions adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.questions_recycler);
        adapter = new RecyclerAdapterQuestions(getActivity(),data);

        initializeData(adapter);

        recyclerView.setAdapter(adapter);

        return rootView;
    }



    public void initializeData(RecyclerAdapterQuestions adapter){
        //Preferences to fetch all schedule data
        SharedPreferences dataPrefs = getActivity().getSharedPreferences("QuestionsData", getActivity().MODE_PRIVATE);
        String schedule = dataPrefs.getString("questions_data", "");

        try {
            JSONArray scheduleDataArray = new JSONArray(schedule);
            for (int i = 0; i<scheduleDataArray.length(); i++){
                JSONObject scheduleData = scheduleDataArray.getJSONObject(i);



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
