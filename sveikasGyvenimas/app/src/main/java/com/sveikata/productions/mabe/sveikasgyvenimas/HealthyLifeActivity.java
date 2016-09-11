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
    private RecycleAdapter adapter;

    //OBJECTS for checking if user is admin
    private JSONArray jsonArray;
    private JSONObject userData;
    private String checkingIfAdmin;

    //OBJECTS for loading schedule data into recyclerView
    private JSONArray scheduleDataArray;
    private JSONObject scheduleData;
    private String schedule;

    private String is_administrator;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Preferences to check if user is admin
        SharedPreferences userPrefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
        String user_data = userPrefs.getString("user_data", "");

        //Preferences to fetch all schedule data
        SharedPreferences dataPrefs = getActivity().getSharedPreferences("ScheduleData", getActivity().MODE_PRIVATE);
        String schedule = dataPrefs.getString("schedule_data", "");


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
            adapter = new RecycleAdapter(getActivity(), data);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        

        } else{
            rootView = inflater.inflate(R.layout.activity_schedule_layout,container,false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_client);
            adapter = new RecycleAdapter(getActivity(), data);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        }

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        mapfragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapfragment == null) {
            mapfragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, mapfragment).commit();

            mapfragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMaps = googleMap;
                    initilizeMap();
                }
            });

        }
    }

    private void initilizeMap() {
            googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMaps.getUiSettings().setMyLocationButtonEnabled(true);
            googleMaps.getUiSettings().setCompassEnabled(true);
            googleMaps.getUiSettings().setRotateGesturesEnabled(true);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(55.5, 23.7)).zoom(6.25f).build();
            googleMaps.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // check if map is created successfully or not
            if (googleMaps == null) {
                Toast.makeText(HealthyLifeActivity.this.getActivity(),
                        "Atsiprašome! Jums žemėlapis neveikia.", Toast.LENGTH_SHORT)
                        .show();
            }

    }

}
