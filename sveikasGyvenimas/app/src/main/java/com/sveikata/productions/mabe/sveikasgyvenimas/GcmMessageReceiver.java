package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class GcmMessageReceiver extends FirebaseMessagingService {


    public GcmMessageReceiver() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data = remoteMessage.getData();



        String type = String.valueOf(data.get("type"));
        
        if(type.equals("message_for_all")){
            String message = String.valueOf(data.get("message"));
            String description= String.valueOf(data.get("description"));
            sendNotificationGeneral(message, description);
        }

        if(type.equals("event")){
            String event_message = String.valueOf(data.get("message"));
            String event_description= String.valueOf(data.get("description"));
            sendEvent(event_message, event_description);
        }

        if(type.equals("challenge")){
            String title = String.valueOf(data.get("title"));
            String challenge_body = String.valueOf(data.get("challenge"));
            sendNotificationChallenge(title, challenge_body);

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("UPDATE_REQUIRED"));
        }

        if(type.equals("facts")){
            String message = String.valueOf(data.get("message"));
            String description= String.valueOf(data.get("description"));
            sendFactNotif(message, description);
        }
//        if(type.equals("faq")){
//            String title = String.valueOf(data.get("faq_title"));
//            String body = String.valueOf(data.get("faq_body"));
//
//            SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//
//            try {
//                JSONArray current_faq = new JSONArray(sharedPreferences.getString("faq_data", ""));
//                JSONObject obj = new JSONObject();
//                obj.put("faq_title", title);
//                obj.put("faq_body", body);
//                current_faq.put(current_faq.length(), obj);
//
//                editor.putString("faq_data", current_faq.toString());
//                editor.commit();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        if(type.equals("faq_remove")){
//
//            String title = String.valueOf(data.get("faq_title"));
//
//            SharedPreferences sharedPreferences = getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//
//            JSONArray current_faq = null;
//            try {
//                current_faq = new JSONArray(sharedPreferences.getString("faq_data", ""));
//
////                Log.i("TEST", current_faq.toString());
//
//                for(int i = 0; i < current_faq.length(); i++){
//                    JSONObject obj = current_faq.getJSONObject(i);
//
//                    if(obj.getString("faq_title").equals(title)){
//                        current_faq = CheckingUtils.RemoveJSONArray(current_faq, i);
//                    }
//                }
//
////                Log.i("TEST", current_faq.toString());
//                editor.putString("faq_data", current_faq.toString());
//                editor.commit();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            editor.putString("faq_data", current_faq.toString());
//            editor.commit();
//
//        }
        super.onMessageReceived(remoteMessage);
    }

    private void sendNotificationGeneral(String message, String description) {

        Intent intent = new Intent(this, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            notificationBuilder.setSmallIcon(R.drawable.icon_for_notif)
                    .setContentTitle(message)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(Color.parseColor("#009688"))
                    .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


    private void sendEvent(String message, String description) {

        Intent intent = new Intent(this, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setSmallIcon(R.drawable.icon_for_notif)
                .setContentTitle(message)
                .setContentText(description)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#009688"))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


    private void sendNotificationChallenge(String title, String body) {
        Intent intent = new Intent(this, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.icon_for_notif)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#e67e22"))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }

    private void sendFactNotif(String title, String body) {
        Intent intent = new Intent(this, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.icon_for_notif)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#3498db"))
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


}