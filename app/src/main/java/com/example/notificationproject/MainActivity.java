package com.example.notificationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button nNotifButton;
    private Button nUpdateButton;
    private Button nCancelButton;

    private static final String CHANNEL_ID = BuildConfig.APPLICATION_ID + "notification-chanel";
    private NotificationManager nNotificationManager;
    private static final int NOTIF_ID = 0;

    private static final String UPDATE_EVENT = "UPDATE_EVENT";
    private NotificationReceiver nNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nNotificationReceiver = new NotificationReceiver();
        registerReceiver(nNotificationReceiver, new IntentFilter(UPDATE_EVENT));

        nNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "notifictaion", NotificationManager.IMPORTANCE_DEFAULT);
            nNotificationManager.createNotificationChannel(channel);
        }

        nNotifButton = findViewById(R.id.notif_btn);
        nUpdateButton = findViewById(R.id.update_btn);
        nCancelButton = findViewById(R.id.cancel_btn);

        nNotifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        nUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        nCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });

        nNotifButton.setEnabled(true);
        nUpdateButton.setEnabled(false);
        nCancelButton.setEnabled(false);
    }

    private void sendNotification(){
        Intent contentIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder built = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        built.setContentTitle("You've been notified!");
        built.setContentTitle("This is notification text.");
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_HIGH);

        String GUIDE_URL = "https://developer.android.com/design/patterns/notifications.html";
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GUIDE_URL));
        PendingIntent pendingLearnMoreIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, learnMoreIntent, PendingIntent.FLAG_ONE_SHOT);

        built.addAction(R.drawable.ic_notification, "Learn More", pendingLearnMoreIntent);

        Intent updateIntent = new Intent(UPDATE_EVENT);
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIF_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        built.addAction(R.drawable.ic_notification, "UPDATE", pendingUpdateIntent);

        Notification notif = built.build();
        nNotificationManager.notify(NOTIF_ID, notif);

        nNotifButton.setEnabled(false);
        nUpdateButton.setEnabled(true);
        nCancelButton.setEnabled(true);
    }

    private void cancelNotification() {
        nNotificationManager.cancel(NOTIF_ID);

        nNotifButton.setEnabled(true);
        nUpdateButton.setEnabled(false);
        nCancelButton.setEnabled(false);
    }

    private void updateNotification() {
        Intent contentIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(),NOTIF_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder built = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        built.setContentTitle("You've been notified!");
        built.setContentTitle("This is notification text.");
        built.setSmallIcon(R.drawable.ic_notification);
        built.setContentIntent(pendingContentIntent);
        built.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_android);
        built.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mascotBitmap).setBigContentTitle("The notification has been updated!"));

        Notification notif = built.build();
        nNotificationManager.notify(NOTIF_ID, notif);

        nNotifButton.setEnabled(false);
        nUpdateButton.setEnabled(false);
        nCancelButton.setEnabled(true);
    }

    public class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == UPDATE_EVENT){
                updateNotification();
            }
        }
    }
}