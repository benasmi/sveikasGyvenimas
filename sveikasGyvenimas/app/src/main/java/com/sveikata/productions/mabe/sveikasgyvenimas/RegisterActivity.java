package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText name_ET;
    private EditText last_name_ET;
    private EditText password_ET;
    private EditText repeat_password_ET;
    private EditText mail_ET;
    private EditText username_ET;
    private Spinner years;
    private Spinner gender;

    private double longtitude;
    private double latitude;


    //Entered value
    private String name_value;
    private String last_name_value;
    private String mail_value;
    private String username_value;
    private String password_value;
    private String repeat_password_value;
    private String years_value;
    private String gender_value;

    private TextView register_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //EditTexts
        name_ET = (EditText) findViewById(R.id.name);
        last_name_ET = (EditText) findViewById(R.id.last_name);
        password_ET = (EditText) findViewById(R.id.password);
        repeat_password_ET = (EditText) findViewById(R.id.repeat_password);
        mail_ET = (EditText) findViewById(R.id.mail);
        username_ET = (EditText) findViewById(R.id.username);


        register_txt = (TextView) findViewById(R.id.register_textview);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/bevan.ttf");
        register_txt.setTypeface(tf);


        //Spinners
        years = (Spinner) findViewById(R.id.years);
        ArrayAdapter<CharSequence> years_adapter = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(years_adapter);

        gender = (Spinner) findViewById(R.id.gender);
        final ArrayAdapter<CharSequence> gender_adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(gender_adapter);




    }



    public void register(View view) {
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        years_value = years.getSelectedItem().toString();
        gender_value = gender.getSelectedItem().toString();
        name_value = name_ET.getText().toString();
        last_name_value = last_name_ET.getText().toString();
        username_value = username_ET.getText().toString();
        password_value = password_ET.getText().toString();
        repeat_password_value = repeat_password_ET.getText().toString();
        mail_value = mail_ET.getText().toString();
        String hometown = "";
        if(username_value.isEmpty()){
            CheckingUtils.vibrate(this, 100);
            username_ET.setError("Prašome įvesti vartotojo vardą");
            return;
        }

        if(username_value.matches(".*\\s+.*")){
            CheckingUtils.vibrate(this, 100);
            username_ET.setError("Negali būti tarpelių");
            return;
        }

        if(name_value.isEmpty()){
            CheckingUtils.vibrate(this, 100);
            name_ET.setError("Įveskite savo vardą");
            return;
        }

        if(name_value.matches(".*\\s+.*")){
            CheckingUtils.vibrate(this, 100);
            name_ET.setError("Negali būti tarpelių");
            return;
        }

        if(last_name_value.isEmpty()){
            CheckingUtils.vibrate(this, 100);
            last_name_ET.setError("Įveskite savo pavardę");
            return;
        }

        if(last_name_value.matches(".*\\s+.*")){
            CheckingUtils.vibrate(this, 100);
            last_name_ET.setError("Negali būti tarpelių");
            return;
        }

        if(mail_value.isEmpty() || !mail_value.contains("@") || mail_value.matches(".*\\s+.*")){
            CheckingUtils.vibrate(this, 100);
            mail_ET.setError("Įveskite galiojantį pašto adresą");
            return;
        }


        if(password_value.isEmpty()){
            CheckingUtils.vibrate(this, 100);
            password_ET.setError("Įveskite slaptažodį");
            return;
        }

        if(password_value.matches(".*\\s+.*")){
            CheckingUtils.vibrate(this, 100);
            password_ET.setError("Negali būti tarpelių");
            return;
        }

        if(!password_value.equals(repeat_password_value)){
            CheckingUtils.vibrate(this, 100);
            password_ET.setError("Slaptažodžiai nesutampa");
            repeat_password_ET.setError("Slaptažodžiai nesutampa");
            return;
        }

        if(years_value.equals("Metai")){
            CheckingUtils.vibrate(this, 100);
            TextView errorText = (TextView) years.getSelectedView();
            errorText.setError("Pasirinkite metus");
            errorText.startAnimation(shake);
            return;

        }
        if(gender_value.equals("Lytis")) {
            CheckingUtils.vibrate(this, 100);
            TextView errorText = (TextView) gender.getSelectedView();
            errorText.setError("Pasirinkite lytį");
            errorText.startAnimation(shake);
            return;
        }

        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.vibrate(this, 100);
            CheckingUtils.createErrorBox("Nori susikurti paskyrą? Įjunk WI-FI arba mobilius )",this, R.style.ScheduleDialogStyle);
            return;
        }

        if(!CheckingUtils.isGpsEnabled(this)){
            CheckingUtils.buildAlertMessageNoGps("Trumpam įjunkite GPS, reikalinga informacija statistiniams duomenims :)",this, R.style.ScheduleDialogStyle);
            return;
        }

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                longtitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLongitude();
                latitude = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getLatitude();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });



        SharedPreferences sharedPrefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String token = sharedPrefs.getString("device_id", "");

        try {
            hometown = CheckingUtils.address(longtitude, latitude, this);
            Log.i("TEST", hometown);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ServerManager(this, "REGISTRATION").execute("REGISTRATION",name_value,last_name_value,username_value,password_value,mail_value,gender_value,years_value,"regular", token, hometown);

    }

    public void goBackToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class).putExtra("isAnimDisabled", false));
    }


}
