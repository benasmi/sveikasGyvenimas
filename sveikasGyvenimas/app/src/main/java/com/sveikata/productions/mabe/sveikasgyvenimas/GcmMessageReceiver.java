package com.sveikata.productions.mabe.sveikasgyvenimas;

/**
 * Created by Benas on 9/15/2016.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;




/**
 * Created by Benas on 6/19/2016.
 */
public class GcmMessageReceiver extends GcmListenerService {


    public GcmMessageReceiver() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {


        String message = data.getString("message");
        String description = data.getString("description");

        sendNotification(message, description);






    }

    private void sendNotification(String message, String description) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

            notificationBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(message)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


}