package com.clothly.ecommerce.data.ui.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PushNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message= remoteMessage.getData().get("message");

        try {
            if(message != null) {
                JSONObject jsonObject = new JSONObject(message);
                showNotification(jsonObject.getString("title"), jsonObject.getString("desc"));
                }
            else {
                showNotification("TripEasy", message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (remoteMessage.getNotification() != null) {
            //  Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //showNotofication(message);

    }

    public void showNotification(String title, String message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.color.transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_squre))
                .setSound(alarmSound)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
