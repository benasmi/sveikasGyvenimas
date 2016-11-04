package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class PlayActivity extends android.support.v4.app.Fragment {

    public static boolean shouldAddInfo = true;
    public static RecyclerView recyclerView;
    private ArrayList<PlayInfoHolder> info = new ArrayList<PlayInfoHolder>();
    private JSONArray challenges;
    private boolean isReceiverRegistered = false;
    private BroadcastReceiver broadcastReceiver;
    private Context ctx;
    private boolean show = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_play,container,false);
        ctx = getContext();
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
            addHumanOrgans();
            addStartQuiz();
            addHigherLower();
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

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                ServerManager manager = new ServerManager(context, "");
                manager.setOnFinishListener(new OnFinishListener() {
                    @Override
                    public void onFinish(int responseCode) {
                        if(isAdded()){
                            startActivity(new Intent(context, TabActivityLoader.class).putExtra("Tab", 1));
                        }
                    }
                });
                manager.startFetchingData(1, false);

            }
        };

        registerReceiver();
        return rootView;
    }

    public void addCalculatorPreview(){
        info.add(new PlayInfoHolder(0));
    }

    public void addSendChallenge(){
        info.add(new PlayInfoHolder(2));
    }
    public void addHigherLower(){
        info.add(new PlayInfoHolder(5));
    }

    public void addStartQuiz(){
        info.add(new PlayInfoHolder(6));
    }

    public void addHumanOrgans(){
        info.add(new PlayInfoHolder(7));
    }




    public void addChallenge() throws JSONException {

        for(int i = 0; i < challenges.length(); i++){
            JSONObject challenge = challenges.getJSONObject(i);

            String description = challenge.getString("challenge");
            String time = challenge.getString("time");
            String title = challenge.getString("challenge_title");
            String sender = challenge.getString("challenge_sender");

            if(challenge.getString("challenge_state").equals("0")){
                info.add(new PlayInfoHolder(1, description, time, title, sender));
            }
            if(challenge.getString("challenge_state").equals("2")){
                info.add(new PlayInfoHolder(4, description, time, title, sender));
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
                String sender = challenge.getString("challenge_sender");

                if(challenge_state.equals("1")){
                    info.add(new PlayInfoHolder(3, description, time, title, sender));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                    new IntentFilter("UPDATE_REQUIRED"));
            isReceiverRegistered = true;
        }
    }

}
