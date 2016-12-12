package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudrail.si.CloudRail;
import com.cloudrail.si.services.Facebook;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.Arrays;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class LoginActivity extends AppCompatActivity {


    private EditText username_ET;
    private EditText password_ET;
    public static final String TAG = "TEST";

    //Facebook login widget init
    private Button loginButton;
    private CallbackManager callbackManager;

    //Google plus login widget init
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int REQUEST_CODE_FOR_GOOGLE = 1;

    //Sharedprefs
    private SharedPreferences sharedPrefs;
    private SharedPreferences loginPrefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Facebook init
        CloudRail.setAppKey("584ae79ab4c211193a8472d9");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);




        //Google+ init
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        CheckingUtils.createErrorBox("Oooops! Bandyk dar kartą.", LoginActivity.this, R.style.ScheduleDialogStyle);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...








        sharedPrefs = getSharedPreferences("UserData", MODE_PRIVATE);
        loginPrefs = getSharedPreferences("DataPrefs", MODE_PRIVATE);
        setContentView(R.layout.activity_login);

        if(!getIntent().getExtras().getBoolean("isAnimDisabled", false))
            overridePendingTransition(R.anim.slide_in_long, R.anim.slide_out_long);


        username_ET = (EditText) findViewById(R.id.login_username_text);
        password_ET = (EditText) findViewById(R.id.login_password_text);


        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/bevan.ttf");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setTypeface(tf);



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Login with facebook
    public void onfbClick(View view) {
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile", "user_hometown", "user_birthday"));
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//
//            @Override
//            public void onSuccess(final LoginResult loginResult) {
//                AccessToken accessToken = loginResult.getAccessToken();
//                Profile profile = Profile.getCurrentProfile();
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "first_name,last_name,email,gender,hometown,birthday");
//
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                //Log.i("TEST", response.getError().toString());
//
//                                try {
//                                    String name = object.getString("first_name");
//                                    String last_name = object.getString("last_name");
//                                    String username = object.getString("id");
//                                    String password = loginResult.getAccessToken().getToken().toString();
//                                    String mail = object.getString("email");
//                                    String gender = object.getString("gender");
//                                    String years = object.getString("birthday");
//                                    JSONObject hometown = object.getJSONObject("hometown");
//                                    String town = hometown.getString("name");
//                                    String type = "facebook";
//                                    String token = sharedPrefs.getString("device_id", "");
//
//                                    new ServerManager(LoginActivity.this, "LOGIN_GMAIL_AND_REGISTER").execute("LOGIN_GMAIL_AND_REGISTER", username, password, token, name, last_name, years, gender, mail, type, town);
//
//                                }catch (Exception e ){
//                                    Log.i("TEST", "ERROR");
//                                }
//                            }
//                        });
//
//
//
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Log.i("TEST", "cancelTriggered");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.i("TEST", "errorTriggered");
//                exception.printStackTrace();
//
//            }
//        });



        new AsyncTask<String, String, String>(){

            String username;
            String password;
            String mail;
            String gender;
            String years;
            String town;
            String type;
            String name = "";
            String last_name = "";
            String token;

            @Override
            protected String doInBackground(String... params) {
                Facebook facebook = new Facebook(LoginActivity.this, getString(R.string.facebook_app_id), getString(R.string.facebook_secret));

                try{
                    for(int i = 0; i < facebook.getFullName().length(); i++){
                        if(facebook.getFullName().charAt(i) == ' '){
                            name = facebook.getFullName().substring(0, i);
                            last_name = facebook.getFullName().substring(i, facebook.getFullName().length());
                        }
                    }

                    username = facebook.getFullName();
                    password = facebook.getIdentifier();
                    mail = facebook.getEmail();
                    gender = facebook.getGender();
                    years = facebook.getDateOfBirth().toString();
                    type = "facebook";
                    token = sharedPrefs.getString("device_id", "");
                    return null;

                }catch (Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {

                if(years != null){
                    new ServerManager(LoginActivity.this, "LOGIN_GMAIL_AND_REGISTER").execute("LOGIN_GMAIL_AND_REGISTER", username, password, token, name, last_name, years, gender, mail, type, "Unknown");
                }

//                final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                super.onPostExecute(s);
            }
        }.execute();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_FOR_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.i("TEST", result.getStatus().toString());
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();



                String name = account.getGivenName();
                String last_name = account.getFamilyName();
                String username = account.getId();
                String password = account.getIdToken();
                String mail = account.getEmail();
                String gender = "unknown";
                String years = "18";
                String type = "gmail";
                String token = sharedPrefs.getString("device_id", "");

                new ServerManager(LoginActivity.this, "LOGIN_GMAIL_AND_REGISTER").execute("LOGIN_GMAIL_AND_REGISTER", username, password, token, name, last_name,years,gender,mail,type, "Unknown");
                firebaseAuthWithGoogle(account);

                }else{
                    Log.i("TEST", "ASFASFASFASSAASFASFFSA");
                Log.i("TEST", result.toString());
                }

            } else {

            }
        }





    public void logIn(View view){
        String username = username_ET.getText().toString().trim();
        String password = password_ET.getText().toString().trim();
        String device_id = sharedPrefs.getString("device_id", "");


        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Nejaugi gaila mobilių?...", this, R.style.ScheduleDialogStyle);
            return;
        }

        if(username.isEmpty()){
            username_ET.setError("Noooope!");
            return;
        }
        if(password.isEmpty()){
            password_ET.setError("Dar didesnis nope!");
            return;
        }


        new ServerManager(this, "LOGIN").execute("LOGIN", username,password, device_id, "regular");

    }





    private void signIn() {
                revokeAccess();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void googleSignIn(View view) {
        signIn();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                            startActivityForResult(signInIntent, REQUEST_CODE_FOR_GOOGLE);

                    }
                });
    }

    public void register(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void forgot_password(View view){

        startActivity(new Intent(this, ForgotPassword.class));

    }



}
