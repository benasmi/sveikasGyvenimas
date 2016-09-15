package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

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

    private static final String[] TOPICS = {"global"};
    private SharedPreferences sharedPreferences;
    public static String SERVER_ADRESS_INSERT_TOKEN = "http://dvp.lt/android/insert_token.php";
    public RegisterTokenService() {
        super("RegisterTokenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

            String old_id = sharedPreferences.getString("device_id", "");
            sharedPreferences.edit().putString("old_device_id", old_id).commit();


            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_api),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sharedPreferences.edit().putString("device_id", token).commit();

            Log.i("TEST", token);

            subscribeTopics(token);

            if(!old_id.equals(token)){
                update_device_id();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    //Update device_id method
    private void update_device_id() {

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://bm.prestanotifications.com/update_token.php");

        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.putOpt("email_username", sharedPreferences.getString("username", ""));
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