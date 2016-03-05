package com.nomapp.nomapp_beta.Alarms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.nomapp.nomapp_beta.R;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Alarm Raised", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent();
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, intent1, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.icon);
        builder.setContentTitle("Тест");
        builder.setContentText("Тест");
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setLights(0x0000FF,3000,2000);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(56, builder.build());
    }
}
