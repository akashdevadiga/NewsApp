package com.thedascapital.www.newsapp.Services;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thedascapital.www.newsapp.R;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static String TAG = "FCM";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //remoteMessage.getNotification().getTitle();
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(), remoteMessage.getNotification().getBody());
    }

    public void showNotification(String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                                                .setContentTitle(title)
                                                .setSmallIcon(R.drawable.ic_app_logo)
                                                .setAutoCancel(true)
                                                .setContentText(message);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(999, builder.build());
    }


}
