package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PlayActivity extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private ArrayList<PlayInfoHolder> info = new ArrayList<PlayInfoHolder>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_play,container,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.play_recycler_view);

        PlayAdapter adapter = new PlayAdapter(getActivity(), info);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        addCalculatorPreview();

        recyclerView.setAdapter(adapter);



        return rootView;
    }

    public void addCalculatorPreview(){
        info.add(0, new PlayInfoHolder(0));
    }
}
