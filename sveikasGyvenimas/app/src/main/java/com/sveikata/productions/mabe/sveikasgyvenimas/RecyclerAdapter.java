package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Martyno on 2016.09.10.
 */
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<InfoHolder> infoHolder;
    private Fragment fragment;
    private GoogleMap googleMaps;

    public RecyclerAdapter(Context context, ArrayList<InfoHolder> infoHolder, Fragment fragment) {
        this.infoHolder = infoHolder;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;

    }

    public void remove(int position) {
        infoHolder.remove(position);
        notifyItemRemoved(position);

    }
    @Override
    public int getItemViewType(int position) {
        InfoHolder data = infoHolder.get(position);
        int type = Integer.valueOf(data.getRecycler_view_type());
        return type;
    }


    public void add(InfoHolder info) {
        infoHolder.add(0,info);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return infoHolder.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View schedule = layoutInflater.inflate(R.layout.schedule_item, parent, false);
                ViewHolder schedule_item = new ViewHolder(schedule, 0);
                return schedule_item;

            case 1:
                View view = layoutInflater.inflate(R.layout.layout_map, parent, false);
                ViewHolder holder = new ViewHolder(view, 1);
                return holder;


            case 2:
                View view1 = layoutInflater.inflate(R.layout.layout_description, parent, false);
                ViewHolder holder1 = new ViewHolder(view1, 2);
                return holder1;

        }


        View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view, 0);
        return holder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final InfoHolder data = infoHolder.get(position);
        String dataType = data.getRecycler_view_type();

        if(dataType.equals("0")){
            holder.event_date_and_place.setText(data.getEvent_location_and_date());
            holder.event_name.setText(data.getEvent_name());
            holder.event_description.setText(data.getEvent_description());

            if(position % 2 == 0) {
                holder.layout.setBackgroundColor(Color.parseColor("#FAFAFA"));
            }
        }
        if(dataType.equals("1")){

            holder.map.onCreate(null);
            holder.map.onResume();
            holder.map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMaps = googleMap;
                    googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMaps.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMaps.getUiSettings().setAllGesturesEnabled(false);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(55.3, 23.7)).zoom(5.8f).build();


                    googleMaps.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    while(data.getLatitude() !=0 ){
                        googleMaps.addMarker(new MarkerOptions().position(new LatLng(data.getLatitude(),data.getLongtitude())).title(data.getEvent_name()).snippet(data.event_location_and_date));
                    }

                    if (googleMaps != null) {

                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View view = layoutInflater.inflate(R.layout.marker_info, null);
                        googleMaps.setInfoWindowAdapter(new MarkerInfoClass(view));
                    }

                    googleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker.showInfoWindow();
                            return false;
                        }
                    });

                    // check if map is created successfully or not
                    if (googleMaps == null) {
                        Toast.makeText(context,
                                "Atsiprašome! Jums žemėlapis neveikia.", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            });


        }
        if(dataType.equals("2")){

            holder.map.onCreate(null);
            holder.map.onResume();
            holder.map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMaps = googleMap;
                    googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMaps.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMaps.getUiSettings().setAllGesturesEnabled(false);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(55.3, 23.7)).zoom(5.8f).build();


                    googleMaps.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    while(data.getLatitude()!=0){
                        googleMaps.addMarker(new MarkerOptions().position(new LatLng(data.getLatitude(),data.getLongtitude())).title(data.getEvent_name()).snippet(data.event_location_and_date));
                    }

                    if (googleMaps != null) {

                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View view = layoutInflater.inflate(R.layout.marker_info, null);
                        googleMaps.setInfoWindowAdapter(new MarkerInfoClass(view));
                    }

                    googleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            marker.showInfoWindow();
                            return false;
                        }
                    });

                    // check if map is created successfully or not
                    if (googleMaps == null) {
                        Toast.makeText(context,
                                "Atsiprašome! Jums žemėlapis neveikia.", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            });

        }

    }
    class ViewHolder extends RecyclerView.ViewHolder {

        //Map objects

        private SupportMapFragment mapfragment;

        //Schedule item layout
        private LinearLayout layout;
        private TextView event_name;
        private TextView event_date_and_place;
        private TextView event_description;
        private boolean isClicked = true;

        //Admin Map layout
        private EditText event_name_admin;
        private EditText event_date_admin;
        private EditText event_description_admin;
        private EditText event_place_admin;


        //Client Map layout
        private GoogleMap googleMaps;
        private MapView map;



        public ViewHolder(View itemView, int type) {

            super(itemView);

            switch (type) {
                case 0:
                    event_date_and_place = (TextView) itemView.findViewById(R.id.event_date_and_place);
                    event_name = (TextView) itemView.findViewById(R.id.event_name);
                    event_description = (TextView) itemView.findViewById(R.id.event_description);
                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");
                    event_name.setTypeface(tf);
                    layout = (LinearLayout) itemView.findViewById(R.id.text_wrap);


                    break;
                case 1:
                    event_name_admin = (EditText) itemView.findViewById(R.id.event_name_admin);
                    event_date_admin = (EditText) itemView.findViewById(R.id.event_date_admin);
                    event_description_admin = (EditText) itemView.findViewById(R.id.event_description_admin);
                    event_place_admin = (EditText) itemView.findViewById(R.id.event_location_admin);


                    map = (MapView) itemView.findViewById(R.id.map_container);

                    break;


                case 2:
                    map = (MapView) itemView.findViewById(R.id.map_container_client);

                    break;

            }

        }

    }



}

