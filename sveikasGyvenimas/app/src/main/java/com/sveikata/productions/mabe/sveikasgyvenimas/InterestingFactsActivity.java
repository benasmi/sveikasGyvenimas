package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InterestingFactsActivity extends android.support.v4.app.Fragment {


    private ArrayList<FactInfoHolder> data = new ArrayList<FactInfoHolder>();
    private RecyclerView recyclerView;
    private InterestingFactsAdapter adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;

    public static boolean addFactsFirstTime = true;

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


        rootView = inflater.inflate(R.layout.activity_intresting_facts, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_facts);
        adapter = new InterestingFactsAdapter(getActivity(), data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        if (addFactsFirstTime) {
            addFactsFirstTime = false;


            if (is_administrator.equals("1")) {
                initializeDataFirstTime(adapter, 1);
                initializeData(adapter);

            } else {
                initializeData(adapter);
            }

        }

        final SwipeRefreshLayout refresh_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout_facts);
        refresh_layout.setEnabled(true);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ServerManager manager = new ServerManager(getActivity(), "");
                manager.setOnFinishListener(new OnFinishListener() {
                    @Override
                    public void onFinish() {
                        adapter.clear();

                        if(is_administrator.equals("1")){
                            initializeDataFirstTime(adapter, 1);
                            initializeData(adapter);
                        }else{
                            initializeData(adapter);
                        }
                        refresh_layout.setRefreshing(false);
                    }
                });
                manager.startFetchingData(0);

            }
        });


        return rootView;


    }


        public void initializeData(InterestingFactsAdapter adapter){
            //Preferences to fetch all schedule data
            SharedPreferences factsData = getActivity().getSharedPreferences("FactData", getActivity().MODE_PRIVATE);
            String facts = factsData.getString("fact_data", "");

            try {
                JSONArray scheduleDataArray = new JSONArray(facts);
                for (int i = 0; i<scheduleDataArray.length(); i++){
                    JSONObject factsDataValue = scheduleDataArray.getJSONObject(i);

                    Log.i("TEST", factsDataValue.toString());
                    String title = factsDataValue.getString("title");
                    String body = factsDataValue.getString("body");
                    String url = factsDataValue.getString("url");
                    String source = factsDataValue.getString("source");
                    String height = factsDataValue.getString("height");

                    adapter.add(new FactInfoHolder(title,body,url,source, 0, height), 1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public void initializeDataFirstTime(InterestingFactsAdapter adapter, int type){

            adapter.add(new FactInfoHolder("", "","", "", type, ""), 0);

        }

}
