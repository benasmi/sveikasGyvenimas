package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
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

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class InterestingFactsActivity extends android.support.v4.app.Fragment {


    private ArrayList<FactInfoHolder> data = new ArrayList<FactInfoHolder>();
    public static RecyclerView recyclerView;
    private InterestingFactsAdapter adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;

    public static boolean addFactsFirstTime = true;
    private String is_administrator;
    private View rootView;
    private boolean show;




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
        adapter = new InterestingFactsAdapter(getActivity(), data, is_administrator);

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
                    public void onFinish(int responseCode) {
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
                manager.startFetchingData(0, false);

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

                    String title = factsDataValue.getString("title");
                    String body = factsDataValue.getString("body");
                    String url = factsDataValue.getString("url");
                    String source = factsDataValue.getString("source");
                    int height = factsDataValue.getInt("height");
                    if(String.valueOf(height).equals("")){
                        height=0;
                    }
                    if(is_administrator.equals("1")){
                        adapter.add(new FactInfoHolder(title,body,url,source, 0, height), adapter.getItemCount());
                    }else{
                        adapter.add(new FactInfoHolder(title,body,url,source, 0, height), adapter.getItemCount());

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        public void initializeDataFirstTime(InterestingFactsAdapter adapter, int type){

            adapter.add(new FactInfoHolder("", "","", "", type, 0), 0);

        }

}
