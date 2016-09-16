package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Benas on 9/15/2016.
 */
public class GcmOnTokenRefresh extends FirebaseInstanceIdService {

    private SharedPreferences sharedPreferences;

    public GcmOnTokenRefresh(){
        Log.i("TEST","OnTokenRefresh");

    }

    @Override
    public void onTokenRefresh() {
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String old_id = sharedPreferences.getString("device_id", "");
        editor.putString("old_device_id", old_id).commit();

        String token = FirebaseInstanceId.getInstance().getToken();
        editor.putString("device_id", token).commit();

        Log.i("TEST", token);

        if(!old_id.equals(token)){
           //update_device_id();
        }

        super.onTokenRefresh();
    }

    //Update device_id method
    private void update_device_id() {

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ServerManager.SERVER_ADRESS_UPDATE_TOKEN);

        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("username", sharedPreferences.getString("username", ""));
            jsonObject.putOpt("password", sharedPreferences.getString("password", ""));
            jsonObject.putOpt("device_id", sharedPreferences.getString("device_id", ""));
            jsonObject.putOpt("device_id_old", sharedPreferences.getString("old_device_id", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}