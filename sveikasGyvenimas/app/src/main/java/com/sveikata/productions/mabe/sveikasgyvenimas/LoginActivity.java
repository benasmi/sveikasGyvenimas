package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText username_ET;
    private EditText password_ET;
    private Button loginButton;
    private CallbackManager callbackManager;
    private SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        sharedPrefs = getSharedPreferences("UserData", MODE_PRIVATE);

        setContentView(R.layout.activity_login);

        if(!getIntent().getExtras().getBoolean("isAnimDisabled", false))
            overridePendingTransition(R.anim.slide_down, R.anim.slide_out);


        username_ET = (EditText) findViewById(R.id.login_username_text);
        password_ET = (EditText) findViewById(R.id.login_password_text);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/bevan.ttf");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(tf);



        loginButton = (Button) findViewById(R.id.login_button);


        if(!sharedPrefs.getString("password", "").isEmpty())
            new ServerManager(this).startFetchingData();


    }
    public void onfbClick(View view) {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile", "user_location"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(final LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,location,gender,age_range");

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.i("TEST", object.toString());
                                try {
                                    String name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String username = object.getString("id");
                                    String password = loginResult.getAccessToken().getToken().toString();
                                    String mail = object.getString("email");
                                    String gender = object.getString("gender");
                                    String years = "15";
                                    String type = "fb";

                                    new ServerManager(LoginActivity.this).execute("REGISTRATION", name, last_name, username, password, mail, gender, years, type);
                                    sharedPrefs.edit().putString("username", username).commit();
                                    sharedPrefs.edit().putString("password", password).commit();
                                }catch (Exception e ){
                                    Log.i("TEST", "ERROR");
                                }
                            }
                        });

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("TEST", "cancelTriggered");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("TEST", "errorTriggered");

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    public void logIn(View view){
        String username = username_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();
        String device_id = sharedPrefs.getString("device_id", "");

        Log.i("TEST", "Device id " + device_id);
        if(username.isEmpty()){
            username_ET.setError("Įveskite vartotojo vardą");
            return;
        }
        if(password.isEmpty()){
            password_ET.setError("Įveskite slaptažodį");
            return;
        }

        new ServerManager(this).execute("LOGIN", username,password, device_id);

    }

    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
