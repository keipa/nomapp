package com.nomapp.nomapp_beta.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.NotificationListenerService;

import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

/**
 * Created by antonid on 10.02.2016.
 */
public class TimeNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.alexanderklimov.ru/android/"));

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Посетите мой сайт")
                .setContentText("http://developer.alexanderklimov.ru/android/")
               .setSmallIcon(R.drawable.icon);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
