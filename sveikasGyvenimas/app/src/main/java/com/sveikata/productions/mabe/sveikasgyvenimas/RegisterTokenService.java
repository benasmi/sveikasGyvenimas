package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Benas on 9/15/2016.
 */
public class RegisterTokenService extends IntentService {


    private SharedPreferences sharedPreferences;

    public RegisterTokenService() {
        super("RegisterTokenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {



    }



}