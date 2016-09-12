package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Martyno on 2016.09.10.
 */
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<InfoHolder> infoHolder;
    private Fragment fragment;

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
                ViewHolder holder = new ViewHolder(view, 0);
                return holder;


            case 2:
                View view1 = layoutInflater.inflate(R.layout.schedule_layout, parent, false);
                ViewHolder holder1 = new ViewHolder(view1, 0);
                return holder1;

        }


        View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view, 0);
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InfoHolder data = infoHolder.get(position);
        String dataType = data.getRecycler_view_type();

        if(data.equals("0")){
            holder.event_date_and_place.setText(data.getEvent_location_and_date());
            holder.event_name.setText(data.getEvent_name());
            holder.event_description.setText(data.getEvent_description());
        }
        if(data.equals("2")){
           holder.project_name_client.setText(data.getProject_name());
           holder.project_description_client.setText(data.getProject_description());
        }

    }
    class ViewHolder extends RecyclerView.ViewHolder {

        //Map objects
        private GoogleMap googleMaps;
        private SupportMapFragment mapfragment;

        //Schedule item layout
        private RelativeLayout layout;
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
        private TextView project_name_client;
        private TextView project_description_client;


        public ViewHolder(View itemView, int type) {

            super(itemView);

            switch (type){
                case 0:
                    event_date_and_place = (TextView) itemView.findViewById(R.id.event_date_and_place);
                    event_name = (TextView) itemView.findViewById(R.id.event_name);
                    event_description = (TextView) itemView.findViewById(R.id.event_description);

                    layout = (RelativeLayout) itemView.findViewById(R.id.text_wrap);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ResizeAnimation expand = new ResizeAnimation(view, (int) CheckingUtils.convertPixelsToDp(110, context), (int) CheckingUtils.convertPixelsToDp(55, context));
                            expand.setDuration(200);
                            ResizeAnimation shrink = new ResizeAnimation(view, (int) CheckingUtils.convertPixelsToDp(55, context), (int) CheckingUtils.convertPixelsToDp(110, context));
                            shrink.setDuration(200);
                            view.startAnimation(isClicked ? expand : shrink);

                            isClicked = !isClicked;
                        }
                    });

                    break;
                case 1:
                    event_name_admin = (EditText) itemView.findViewById(R.id.event_name_admin);
                    event_date_admin = (EditText) itemView.findViewById(R.id.event_date_admin);
                    event_description_admin = (EditText) itemView.findViewById(R.id.event_description_admin);
                    event_place_admin = (EditText) itemView.findViewById(R.id.event_location_admin);


                    FragmentManager fm =  fragment.getChildFragmentManager();
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


                case 2:
                    project_name_client = (TextView) itemView.findViewById(R.id.project_name_client);
                    project_description_client = (TextView) itemView.findViewById(R.id.project_description_client);

                    FragmentManager fm1 =  fragment.getChildFragmentManager();
                    mapfragment = (SupportMapFragment) fm1.findFragmentById(R.id.map_container_client);
                    if (mapfragment == null) {
                        mapfragment = SupportMapFragment.newInstance();
                        fm1.beginTransaction().replace(R.id.map_container, mapfragment).commit();

                        mapfragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMaps = googleMap;
                                initilizeMap();
                            }
                        });

                    }

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
                Toast.makeText(context,
                        "Atsiprašome! Jums žemėlapis neveikia.", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


}

