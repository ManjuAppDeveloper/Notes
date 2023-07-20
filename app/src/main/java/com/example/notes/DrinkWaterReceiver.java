package com.example.notes;

import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.os.Build;

public class DrinkWaterReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 123; // Unique ID for the notification
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create and display the notification
        showNotification(context);
    }
    private void showNotification(Context context) {
        // Create an intent for the notification
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        // Build the notification
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(R.drawable.desktop)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.desktop))
                .setContentTitle("Reminder: Drink Water")
                .setContentText("It's time to drink water!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        // Get the notification manager and display the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android 8 and above, create a notification channel
            NotificationChannel channel = new NotificationChannel("drinkwater_channel", "Drink Water Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

