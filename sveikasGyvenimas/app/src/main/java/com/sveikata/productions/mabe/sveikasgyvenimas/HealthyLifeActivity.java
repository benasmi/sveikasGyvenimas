package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class HealthyLifeActivity extends android.support.v4.app.Fragment {

    private GoogleMap googleMaps;
    private SupportMapFragment mapfragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_healthy_life,container,false);



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
