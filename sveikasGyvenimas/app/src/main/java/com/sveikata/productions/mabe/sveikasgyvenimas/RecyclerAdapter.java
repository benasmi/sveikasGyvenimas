package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martyno on 2016.09.10.
 */
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    public Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<InfoHolder> infoHolder;
    private Fragment fragment;
    private GoogleMap googleMaps;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerview;
    public String is_administrator;

    //Map pos
    public LatLng currentPos = new LatLng(55.3, 23.7);
    public float mapZoom = 5.8f;


    public RecyclerAdapter(Context context, ArrayList<InfoHolder> infoHolder, Fragment fragment, RecyclerView recyclerview, String is_administrator) {
        this.infoHolder = infoHolder;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.fragment = fragment;
        this.recyclerview = recyclerview;
        this.is_administrator = is_administrator;

        sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

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

    public void add(InfoHolder info, int position) {
        infoHolder.add(position,info);
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
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        final InfoHolder data = infoHolder.get(position);
        String dataType = data.getRecycler_view_type();

        if(dataType.equals("0")){
            holder.event_date_and_place.setText(data.getEvent_location_and_date());
            holder.event_name.setText(data.getEvent_name());
            holder.event_description.setText(data.getEvent_description());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerview.smoothScrollToPosition(0);

                    currentPos = new LatLng(infoHolder.get(position).getLatitude(), infoHolder.get(position).getLongtitude());
                    mapZoom = 8f;
                    notifyDataSetChanged();

                }
            });



            if(position % 2 == 0) {
                holder.layout.setBackgroundColor(Color.parseColor("#FAFAFA"));
            }



        }

        if(dataType.equals("1") || dataType.equals("2")){
           refreshMap(holder);
        }

    }


    private void refreshMap(ViewHolder holder){

        holder.map.onCreate(null);
        holder.map.onResume();
        holder.map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMaps = googleMap;
                googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMaps.getUiSettings().setMyLocationButtonEnabled(false);
                googleMaps.getUiSettings().setAllGesturesEnabled(false);

                //Zooming into selected marker
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentPos).zoom(mapZoom).build();
                googleMaps.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //Reseting to default values
                mapZoom = 5.8f;
                currentPos = new LatLng(55.3, 23.7);

                //Scrolling to event schedule item on info window click
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for(int i = 0; i < infoHolder.size(); i++){
                            if(marker.getTitle().equals(infoHolder.get(i).getEvent_name())){
                                recyclerview.smoothScrollToPosition(i);
                                break;
                            }
                        }
                    }
                });


                //Adding markers
                for(int i=0;i<infoHolder.size(); i++){
                    InfoHolder info = infoHolder.get(i);
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(info.getLatitude(), info.getLongtitude())).title(info.getEvent_name()).snippet(info.event_location_and_date);
                    googleMaps.addMarker(marker);
                }


                //Seeting marker info windows
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

    public void delete_event(final Context context, final String username, final String password, final String name, final String description, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ar tikrai norite pašalinti renginį ?")
                .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            new ServerManager(context, "DELETE_EVENT").execute("DELETE_EVENT",username, password,name,description);
                            remove(position);
                    }
                })
                .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        builder.show();
        builder.create();
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

        private EditText notif_message;
        private EditText notif_description;

        private AppCompatButton send_notif;
        private AppCompatButton event_add_button;

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

                    if(is_administrator.equals("1")){


                        layout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                String username = sharedPreferences.getString("username", "");
                                String password = sharedPreferences.getString("password", "");
                                String description = infoHolder.get(getAdapterPosition()).getEvent_description();
                                String event_date = infoHolder.get(getAdapterPosition()).getEvent_location_and_date();
                                String name = infoHolder.get(getAdapterPosition()).getEvent_name();


//                                Log.i("TEST", "name: " + name);
//                                Log.i("TEST", " description: " + description );
//                                Log.i("TEST", " date/place: " + event_date );
//                                Log.i("TEST", " position: " + String.valueOf(getAdapterPosition()) );

                                if(!CheckingUtils.isNetworkConnected(context)){
                                    CheckingUtils.createErrorBox("Šiam veiksmui atlikti, reikalinga interneto ryšys", context);
                                    return false;
                                }

                                delete_event(context,username,password,name,description, getAdapterPosition());
                                return true;
                            }
                        });

                    }



                    break;
                case 1:
                    event_name_admin = (EditText) itemView.findViewById(R.id.event_name_admin);
                    event_date_admin = (EditText) itemView.findViewById(R.id.event_date_admin);
                    event_description_admin = (EditText) itemView.findViewById(R.id.event_description_admin);
                    event_place_admin = (EditText) itemView.findViewById(R.id.event_location_admin);

                    notif_message = (EditText) itemView.findViewById(R.id.message_notification);
                    notif_description = (EditText) itemView.findViewById(R.id.description_notification);

                    event_add_button = (AppCompatButton) itemView.findViewById(R.id.add_event);
                    event_add_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String event_name = event_name_admin.getText().toString().trim();
                            final  String event_date = event_date_admin.getText().toString().trim();
                            final String event_description = event_description_admin.getText().toString().trim();
                            final String event_location = event_place_admin.getText().toString().trim();

                            final String username = sharedPreferences.getString("username", "");
                            final String password = sharedPreferences.getString("password", "");



                            GeoApiContext geoContext = new GeoApiContext().setApiKey("AIzaSyD7CmlEdYr_-nU6pNDxik_8FTq-tD53iw8");
                            GeocodingResult[] results = new GeocodingResult[0];
                            try {
                                results = GeocodingApi.geocode(geoContext, event_location).await();
                                final double latitude = results[0].geometry.location.lat;
                                final double longtitude = results[0].geometry.location.lng;

                                if(!CheckingUtils.isNetworkConnected(context)){
                                    CheckingUtils.createErrorBox("Norėdami pridėti renginį, jums reikia įjungti internetą", context);
                                    return;
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                    builder.setMessage("Ar tikrai norite pridėti renginį ?")
                                            .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    event_name_admin.setText("");
                                                    event_date_admin.setText("");
                                                    event_description_admin.setText("");
                                                    event_place_admin.setText("");

                                                    new ServerManager(RecyclerAdapter.this.context, "INSERT_EVENT").execute("INSERT_EVENT", username, password, event_location, event_date, String.valueOf(latitude), String.valueOf(longtitude), event_name, event_description);
                                                    add(new InfoHolder(event_name, event_location + " " + event_date, event_description, "0", latitude, longtitude), 1);

                                                }
                                            })
                                            .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            });
                                    builder.show();
                                    builder.create();
                                }


                            } catch (Exception e) {
                                CheckingUtils.createErrorBox("Adresas neteisingas", RecyclerAdapter.this.context);
                            }



                        }
                    });

                    send_notif = (AppCompatButton) itemView.findViewById(R.id.send_notification);
                    send_notif.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           final String message = notif_message.getText().toString().trim();
                           final String description = notif_description.getText().toString().trim();

                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("Norėdami išsiųsti žinutę visiems vartotojams, jums reikia įjungti internetą", context);
                                return;
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                builder.setMessage("Ar tikrai norite išsiųsti pranešimą visiems vartotojams ?")
                                        .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                notif_message.setText("");
                                                notif_description.setText("");
                                                new ServerManager(context, "SEND_NOTIFICATION").execute("SEND_NOTIFICATION", message, description);
                                            }
                                        })
                                        .setNegativeButton("NE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        });
                                builder.show();
                                builder.create();
                            }
                        }
                    });

                    map = (MapView) itemView.findViewById(R.id.map_container);
                    break;
                case 2:
                    map = (MapView) itemView.findViewById(R.id.map_container_client);
                    break;
            }
        }
    }
}