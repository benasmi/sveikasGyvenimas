package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayActivity extends android.support.v4.app.Fragment {

    public static boolean shouldAddInfo = true;
    private RecyclerView recyclerView;
    private ArrayList<PlayInfoHolder> info = new ArrayList<PlayInfoHolder>();
    private JSONArray challenges;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_play,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.play_recycler_view);

        try{
            challenges = new JSONArray(getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("challenge_data", "[]"));
        }catch(Exception e){
            e.printStackTrace();
        }

        PlayAdapter adapter = new PlayAdapter(getActivity(), info);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Adding views
        if(shouldAddInfo){
            shouldAddInfo=false;
            addCalculatorPreview();
            addSendChallenge();

            try {
                addChallenge();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try{
                addCompletedChallenges();
            }catch(JSONException e){
                e.printStackTrace();
            }


        }

        recyclerView.setAdapter(adapter);



        return rootView;
    }

    public void addCalculatorPreview(){
        info.add(0, new PlayInfoHolder(0));
    }


    public void addSendChallenge(){
        info.add(1, new PlayInfoHolder(2));
    }


    public void addChallenge() throws JSONException {

        for(int i = 0; i < challenges.length(); i++){
            JSONObject challenge = challenges.getJSONObject(i);

            String description = challenge.getString("challenge");
            String time = challenge.getString("time");
            String title = challenge.getString("challenge_title");

            if(challenge.getString("challenge_state").equals("0")){
                info.add(new PlayInfoHolder(1, description, time, title));
            }
            if(challenge.getString("challenge_state").equals("2")){
                info.add(new PlayInfoHolder(4, description, time, title));
            }
        }
    }

    public void addCompletedChallenges() throws JSONException{

        for(int i = 0; i < challenges.length(); i++){
            JSONObject challenge = null;
            try {

                challenge = challenges.getJSONObject(i);
                String description = challenge.getString("challenge");
                String time = challenge.getString("time");
                String title = challenge.getString("challenge_title");
                String challenge_state = challenge.getString("challenge_state");
                if(challenge_state.equals("1")){
                    info.add(new PlayInfoHolder(3, description, time, title));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }




}
