package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.plus.model.people.Person;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

/**
 * Created by Martyno on 2016.09.10.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    public Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<InfoHolder> infoHolder;
    private Fragment fragment;
    private GoogleMap googleMaps;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RecyclerView recyclerview;
    public String is_administrator;
    private boolean showMap;
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
        showMap = sharedPreferences.getBoolean("showMap", false);
        editor = sharedPreferences.edit();
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
            case 0: //Event
                View schedule = layoutInflater.inflate(R.layout.schedule_item, parent, false);
                ViewHolder schedule_item = new ViewHolder(schedule, 0);
                return schedule_item;

            case 1: //Map
                View view = layoutInflater.inflate(R.layout.layout_map, parent, false);
                ViewHolder holder = new ViewHolder(view, 1);
                return holder;


            case 2:
                View view1 = layoutInflater.inflate(R.layout.layout_description, parent, false);
                ViewHolder holder1 = new ViewHolder(view1, 2);
                return holder1;

            case 3: //Admin schedule item
                View admin_shedule = layoutInflater.inflate(R.layout.schedule_item_admin, parent, false);
                ViewHolder schedule_admin_holder = new ViewHolder(admin_shedule, 3);
                return schedule_admin_holder;

            case 4: //Line between finished event and not finished
                View event_line = layoutInflater.inflate(R.layout.line_between_events, parent, false);
                ViewHolder event_line_holder = new ViewHolder(event_line, 4);
                return event_line_holder;

            case 5: //Line between finished event and not finished
                View finished_event = layoutInflater.inflate(R.layout.finished_schedule_item, parent, false);
                ViewHolder finished_event_holder = new ViewHolder(finished_event, 5);
                return finished_event_holder;

        }


        View view = layoutInflater.inflate(R.layout.schedule_item, parent, false);
        ViewHolder holder = new ViewHolder(view, 0);
        return holder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        final InfoHolder data = infoHolder.get(position);
        String dataType = data.getRecycler_view_type();
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_schedule);

        if(dataType.equals("0") || dataType.equals("3")){

            if(dataType.equals("0")){
                holder.event_date_and_place.setText(data.getEvent_location_and_date());
                holder.event_name.setText(data.getEvent_name());
                holder.event_description.setText(data.getEvent_description());
            }
            if(dataType.equals("3")){
                holder.event_date_and_place_field_admin.setText(data.getEvent_location_and_date());
                holder.event_name_field_admin.setText(data.getEvent_name());
                holder.event_description_field_admin.setText(data.getEvent_description());
            }


            holder.layout.startAnimation(animation);

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
        if(dataType.equals("4")){
            Animation slide_from_left = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            holder.finished_event_txt.startAnimation(slide_from_left);
        }


        if(dataType.equals("5")){
            Animation slide_from_left = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
            Animation slide_from_right = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);

            holder.event_name_finished.setText(data.getEvent_name());
            holder.event_date_and_place_finished.setText(data.getEvent_location_and_date());
            holder.event_description_finished.setText(data.getEvent_description());


            if(position % 2 ==0){
                holder.layout_finished.startAnimation(slide_from_right);

            }else{
                holder.layout_finished.startAnimation(slide_from_left);

            }


        }


        if(dataType.equals("1") || dataType.equals("2") || dataType.equals("3")){

            showMap = sharedPreferences.getBoolean("showMap", false);

            try {
            if(!showMap){
                editor.putBoolean("showMap", true).commit();
                new MaterialShowcaseView.Builder((Activity) context)
                        .setTarget(holder.map)
                        .setDismissTextColor(Color.parseColor("#FFEAC73C"))
                        .setShape(new RectangleShape((int)CheckingUtils.convertPixelsToDp(400,context),(int)CheckingUtils.convertPixelsToDp(300, context)))
                        .setShapePadding(-75)
                        .setDismissText("SUPRATAU!")
                        .setContentText("Sveikas atvykęs, mielas vartotojau. Čia visada matysi visus vykstančius renginius, o žemiau galėsi apie juos paskaityti!")
                        .setFadeDuration(1000)
                        .setDelay(1000) // optional but starting animations immediately in onCreate can make them choppy
                        .show();
            }
                refreshMap(holder);

            }catch (Exception e){
            }


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


                googleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                    @Override
                    public void onInfoWindowLongClick(Marker marker) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                        context.startActivity(browserIntent);
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

    public void finish_event(final Context context, final String username, final String password, final String name, final String description){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Ar tikrai norite pabaigti renginį(pridėti prie baigtų renginių skilties)?")
                .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ServerManager(context, "FINISH_EVENT").execute("FINISH_EVENT",username, password,name,description);
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
        private AppCompatButton will_participate;

        //Admin schedule item
        private EditText event_name_field_admin;
        private EditText event_date_and_place_field_admin;
        private EditText event_description_field_admin;
        private ImageView delete_cross_admin;
        private ImageView finish_event_admin;
        private AppCompatImageButton save_button;
        private AppCompatButton will_participate_admin;

        //Admin Map layout
        private EditText event_name_admin;
        private EditText event_date_admin;
        private EditText event_description_admin;
        private EditText event_place_admin;
        private EditText event_fb_link;

        private EditText notif_message;
        private EditText notif_description;

        private AppCompatButton send_notif;
        private AppCompatButton event_add_button;

        //Client Map layout
        private GoogleMap googleMaps;
        private MapView map;

        //Finished event schedule item
        private TextView event_name_finished;
        private TextView event_date_and_place_finished;
        private TextView event_description_finished;
        private LinearLayout layout_finished;

        //Line between evetns
        private TextView finished_event_txt;

        private Typeface tf;
        private Typeface typeface;
        private Typeface verdanaTf;

        public ViewHolder(View itemView, int type) {

            super(itemView);

            switch (type) {
                case 0: //Sending message
                    event_date_and_place = (TextView) itemView.findViewById(R.id.event_date_and_place);
                    event_name = (TextView) itemView.findViewById(R.id.event_name);
                    event_description = (TextView) itemView.findViewById(R.id.event_description);
                    tf = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");
                    event_name.setTypeface(tf);
                    layout = (LinearLayout) itemView.findViewById(R.id.text_wrap);

                    will_participate = (AppCompatButton) itemView.findViewById(R.id.will_participate);

                    will_participate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            participate(infoHolder.get(getAdapterPosition()).getFbPage());
                        }
                    });

                    break;
                case 1: //Adding event
                    event_name_admin = (EditText) itemView.findViewById(R.id.event_name_admin);
                    event_date_admin = (EditText) itemView.findViewById(R.id.event_date_admin);
                    event_description_admin = (EditText) itemView.findViewById(R.id.event_description_admin);
                    event_place_admin = (EditText) itemView.findViewById(R.id.event_location_admin);

                    notif_message = (EditText) itemView.findViewById(R.id.message_notification);
                    notif_description = (EditText) itemView.findViewById(R.id.description_notification);

                    event_fb_link = (EditText) itemView.findViewById(R.id.event_fb_link_admin) ;

                    event_add_button = (AppCompatButton) itemView.findViewById(R.id.add_event);
                    event_add_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String event_name = event_name_admin.getText().toString().trim();
                            final  String event_date = event_date_admin.getText().toString().trim();
                            final String event_description = event_description_admin.getText().toString().trim();
                            final String event_location = event_place_admin.getText().toString().trim();

                            final String  event_fb = event_fb_link.getText().toString().trim();

                            final String username = sharedPreferences.getString("username", "");
                            final String password = sharedPreferences.getString("password", "");



                            GeoApiContext geoContext = new GeoApiContext().setApiKey("AIzaSyD7CmlEdYr_-nU6pNDxik_8FTq-tD53iw8");
                            GeocodingResult[] results = new GeocodingResult[0];
                            try {
                                results = GeocodingApi.geocode(geoContext, event_location).await();
                                final double latitude = results[0].geometry.location.lat;
                                final double longtitude = results[0].geometry.location.lng;

                                if(!CheckingUtils.isNetworkConnected(context)){
                                    CheckingUtils.createErrorBox("Norėdami pridėti renginį, jums reikia įjungti internetą", context, R.style.ScheduleDialogStyle);
                                    return;
                                }else{
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                    if(event_name.isEmpty()){
                                        event_name_admin.setError("Šis laukelis negali būti tuščias!");
                                        return;
                                    }
                                    if(event_date.isEmpty()){
                                        event_date_admin.setError("Šis laukelis negali būti tuščias!");
                                        return;
                                    }

                                    //Checking date format to be yyyy-mm-dd
                                    for(int i = 0; i < event_date.length(); i++) {
                                        try {
                                            Integer.parseInt(String.valueOf(event_date.charAt(i)));
                                        } catch (Exception e) {
                                            if (event_date.charAt(i) != '-') {
                                                event_date_admin.setError("Datos formatas: yyyy-mm-dd");
                                                return;
                                            }
                                        }
                                    }
                                    if(event_date.length() > 10){
                                        event_date_admin.setError("Datos formatas: yyyy-mm-dd");
                                        return;
                                    }


                                    if(event_description.isEmpty()){
                                        event_description_admin.setError("Šis laukelis negali būti tuščias!");
                                        return;
                                    }
                                    if(event_location.isEmpty()){
                                        event_place_admin.setError("Šis laukelis negali būti tuščias!");
                                        return;
                                    }
                                    if(event_fb.isEmpty()){
                                        event_fb_link.setError("Šis laukelis negali būti tuščias!");
                                        return;
                                    }
                                    if(!CheckingUtils.connectionToServer(event_fb, 3000)){
                                        ServerManager mngr = new ServerManager(context, "");

                                        mngr.setOnFinishListener(new OnFinishListener() {
                                            @Override
                                            public void onFinish(int responseCode) {
                                                if(responseCode == 1){
                                                    Log.i("TEST", "response: " + responseCode);
                                                    event_fb_link.setError("Netinkama nuoroda!");
                                                }else{


                                                builder.setMessage("Ar tikrai norite pridėti renginį ?")
                                                        .setPositiveButton("TAIP", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                event_name_admin.setText("");
                                                                event_date_admin.setText("");
                                                                event_description_admin.setText("");
                                                                event_place_admin.setText("");
                                                                event_fb_link.setText("");

                                                                new ServerManager(RecyclerAdapter.this.context, "INSERT_EVENT").execute("INSERT_EVENT", username, password, event_location, event_date, String.valueOf(latitude), String.valueOf(longtitude), event_name, event_description, event_fb, infoHolder.size()>1 ? "0" : "1");

                                                                int id = 0;

                                                                    for (int i = 0; i < infoHolder.size(); i++){
                                                                        try{
                                                                            int unchecked_id = Integer.parseInt(infoHolder.get(i).getId());

                                                                            if(unchecked_id > id){
                                                                                id = unchecked_id;
                                                                            }
                                                                        }catch (Exception e){
                                                                        }
                                                                    }

                                                                add(new InfoHolder(event_name, event_location + " " + event_date, event_description, "3", latitude, longtitude, String.valueOf(id+1), event_fb), 1);


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

                                        mngr.execute("PING", event_fb);


                                        return;
                                    }


                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                CheckingUtils.createErrorBox("Adresas neteisingas", RecyclerAdapter.this.context, R.style.ScheduleDialogStyle);
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
                                CheckingUtils.createErrorBox("Norėdami išsiųsti žinutę visiems vartotojams, jums reikia įjungti internetą", context, R.style.ScheduleDialogStyle);
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
                case 2: //Map
                    map = (MapView) itemView.findViewById(R.id.map_container_client);

                    break;
                case 3:
                    event_date_and_place_field_admin = (EditText) itemView.findViewById(R.id.event_date_and_place_admin);
                    event_name_field_admin = (EditText) itemView.findViewById(R.id.event_name_admin);
                    event_description_field_admin = (EditText) itemView.findViewById(R.id.event_description_admin);
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");
                    event_name_field_admin.setTypeface(typeface);
                    layout = (LinearLayout) itemView.findViewById(R.id.text_wrap_admin);
                    will_participate_admin = (AppCompatButton) itemView.findViewById(R.id.will_participate_admin);
                    delete_cross_admin = (ImageView) itemView.findViewById(R.id.delete_cross_admin);
                    finish_event_admin = (ImageView) itemView.findViewById(R.id.finish_event);
                    save_button = (AppCompatImageButton) itemView.findViewById(R.id.save_event);

                    event_date_and_place_field_admin.setEnabled(false); //Just in case they change their minds

                    //Deleting event
                    delete_cross_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if(!CheckingUtils.isNetworkConnected(context)){
                                    CheckingUtils.createErrorBox("Šiam veiksmui atlikti, reikalingas interneto ryšys", context, R.style.ScheduleDialogStyle);
                                    return;
                                    }

                                    String username = sharedPreferences.getString("username", "");
                                    String password = sharedPreferences.getString("password", "");
                                    String description = infoHolder.get(getAdapterPosition()).getEvent_description();
                                    String event_date = infoHolder.get(getAdapterPosition()).getEvent_location_and_date();
                                    String name = infoHolder.get(getAdapterPosition()).getEvent_name();


                                    delete_event(context,username,password,name,description, getAdapterPosition());
                                }
                    });

                    finish_event_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!CheckingUtils.isNetworkConnected(context)){
                                CheckingUtils.createErrorBox("Šiam veiksmui atlikti, reikalingas interneto ryšys", context, R.style.ScheduleDialogStyle);
                                return;
                            }


                            String username = sharedPreferences.getString("username", "");
                            String password = sharedPreferences.getString("password", "");
                            String description = infoHolder.get(getAdapterPosition()).getEvent_description();
                            String name = infoHolder.get(getAdapterPosition()).getEvent_name();

                                    finish_event(context, username, password, name, description);

                        }
                    });

                    //Saving event
                    save_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String event_name = event_name_field_admin.getText().toString().trim();
                            String event_description = event_description_field_admin.getText().toString().trim();

                            String id = infoHolder.get(getAdapterPosition()).getId();

                            new ServerManager(context, "UPDATE_EVENT").execute("MODIFY_EVENT",id, event_name, event_description);
                        }
                    });

                    //Participating in an event
                    will_participate_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferencesSchedule = context.getSharedPreferences("ScheduleData", Context.MODE_PRIVATE);

                            String id = sharedPreferencesSchedule.getString("id", "-1");

                            participate(infoHolder.get(getAdapterPosition()).getFbPage());
                        }
                    });


                    break;

                case 4:
                    finished_event_txt = (TextView) itemView.findViewById(R.id.finished_events_txt);
                    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Verdana.ttf");
                    finished_event_txt.setTypeface(tf);
                    break;

                case 5:
                    event_name_finished = (TextView) itemView.findViewById(R.id.event_name_finished);
                    event_date_and_place_finished = (TextView) itemView.findViewById(R.id.event_date_and_place_finished);
                    event_description_finished = (TextView) itemView.findViewById(R.id.event_description_finished);

                    layout_finished = (LinearLayout) itemView.findViewById(R.id.text_wrap_finished);
                    typeface = Typeface.createFromAsset(context.getAssets(), "fonts/bevan.ttf");
                    event_name_finished.setTypeface(typeface);
                    break;
            }


        }

        private void participate(String link){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            context.startActivity(i);
        }
    }
}