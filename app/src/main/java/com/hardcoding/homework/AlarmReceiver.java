package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    final public static String ONE_TIME = "onetime";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire(10*60*1000L /*10 minutes*/);

        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();
        Intent intents = new Intent(context, DetailsActivity.class);
        String titles = intent.getStringExtra("title");
        int cancelId = intent.getIntExtra("cancelId", -1);
        int id = intent.getIntExtra("id", 1);


        //Release the lock
        sendNotification(context, "заполните отчет", titles, id, cancelId);
        wl.release();
    }

    public static void sendNotification(Context mcontext, String messageBody, String title, int id, int cancelId) {


        Intent intent = new Intent(mcontext, MainActivity.class);
        intent.putExtra("push", 1);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mcontext, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(mcontext.getString(R.string.default_notification_channel_id), "Rewards Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("HOMEFIT");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 200, 500});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        androidx.core.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mcontext, mcontext.getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());


    }
}

