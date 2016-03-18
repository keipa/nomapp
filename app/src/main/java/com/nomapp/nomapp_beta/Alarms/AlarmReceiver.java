package com.nomapp.nomapp_beta.Alarms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.nomapp.nomapp_beta.R;
import com.nomapp.nomapp_beta.Start.StartActivity;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Toast.makeText(context, "Alarm Raised", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, StartActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, intent1, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_local_dining_grey_18dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon));
        builder.setContentTitle("Что сегодня на ужин?");
        builder.setContentText("Нажмите, чтобы посмотреть.");
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setSound(Uri.EMPTY);
       // builder.setLights(0x0000FF,3000,2000);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(56, builder.build());
    }
}