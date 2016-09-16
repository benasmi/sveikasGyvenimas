package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Benas on 9/15/2016.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by Benas on 6/19/2016.
 */
public class GcmMessageReceiver extends FirebaseMessagingService {


    public GcmMessageReceiver() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data = remoteMessage.getData();

        String message = String.valueOf(data.get("message"));
        String description= String.valueOf(data.get("description"));

        sendNotification(message, description);

        super.onMessageReceived(remoteMessage);

    }

    private void sendNotification(String message, String description) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("isAnimDisabled", true);
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
                    .setColor(Color.argb(1,72,103,170))
                    .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


}