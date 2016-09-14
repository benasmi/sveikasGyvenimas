package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HealthyLifeActivity extends android.support.v4.app.Fragment {

    private GoogleMap googleMaps;
    private SupportMapFragment mapfragment;

    private ArrayList<InfoHolder> data = new ArrayList<InfoHolder>();
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;

    private String is_administrator;
    private View rootView;

    public static boolean addData = true;

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


        if(is_administrator.equals("1")){
            rootView = inflater.inflate(R.layout.activity_schedule_layout_admin,container,false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            adapter = new RecyclerAdapter(getActivity(), data, this);

            if(addData) {
                addData = false;
                initializeData(adapter);
                initializeDataFirstTime(adapter, "1");
            }


            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        } else{
            rootView = inflater.inflate(R.layout.activity_schedule_layout,container,false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_client);
            adapter = new RecyclerAdapter(getActivity(), data,this);

            if(addData) {
                addData = false;
                initializeData(adapter);
                initializeDataFirstTime(adapter, "2");
            }


            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

        return rootView;

    }


    public void initializeData(RecyclerAdapter adapter){
        //Preferences to fetch all schedule data
        SharedPreferences dataPrefs = getActivity().getSharedPreferences("ScheduleData", getActivity().MODE_PRIVATE);
        String schedule = dataPrefs.getString("schedule_data", "");

        try {
           JSONArray scheduleDataArray = new JSONArray(schedule);
            for (int i = 0; i<scheduleDataArray.length(); i++){
                JSONObject scheduleData = scheduleDataArray.getJSONObject(i);

                String event_description = scheduleData.getString("description");
                String event_name = scheduleData.getString("name");
                String event_location = scheduleData.getString("location_name");
                String event_date = scheduleData.getString("date");
                double latitude = scheduleData.getDouble("latitude");
                double longtitude = scheduleData.getDouble("longtitude");

                adapter.add(new InfoHolder(event_name, event_location + event_date, event_description,"0"));
                adapter.add(new InfoHolder(latitude, longtitude));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void initializeDataFirstTime(RecyclerAdapter adapter, String type){
        //Preferences to fetch all schedule data
        SharedPreferences dataPrefs = getActivity().getSharedPreferences("ScheduleData", getActivity().MODE_PRIVATE);
        String schedule = dataPrefs.getString("schedule_data", "");

        try {
                JSONArray scheduleDataArray = new JSONArray(schedule);
                JSONObject scheduleData = scheduleDataArray.getJSONObject(0);

                String event_description = scheduleData.getString("description");
                String event_name = scheduleData.getString("name");
                String event_location = scheduleData.getString("location_name");
                String event_date = scheduleData.getString("date");


                adapter.add(new InfoHolder(event_name, event_location + event_date, event_description,type));


            } catch (JSONException e1) {
            e1.printStackTrace();
        }




    }


}
