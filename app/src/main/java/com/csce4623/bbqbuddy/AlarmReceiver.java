package com.csce4623.bbqbuddy;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.csce4623.bbqbuddy.mainactivity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String ALARM_CHANNEL_ID = "ALARM";

    String itemTitle;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            if (intent.hasExtra("Title")) {
                itemTitle = (String) intent.getStringExtra("Title");
            }
        }

        // ***** TODO: CLICKING ON NOTIFICATION CRASHES APP
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder alarm_builder = new NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Grill Session Reminder!")
                .setContentText(itemTitle)
                .setChannelId(ALARM_CHANNEL_ID);

        // ***** TODO: REDIRECT NOTIFICATION TO SPECIFIC SESSION OR SOMETHING
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm_builder.setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = ALARM_CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "AlarmNotification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            alarm_builder.setChannelId(channelId);
        }

        notificationManager.notify(0, alarm_builder.build());
        Log.d("AlarmReceiver", "notification set");
    }


}
