package com.example.daily.myapplication.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.daily.myapplication.MainActivity;
import com.example.daily.myapplication.R;
import com.example.daily.myapplication.entityClass.Task;

import java.io.File;

public class ClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.example.daily.myapplication.ACTION_SEND")) {
            Intent intent_toMain = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent_toMain = PendingIntent.getActivity(context,0,intent_toMain,0);
            Task aTask = (Task) intent.getSerializableExtra("aTask");
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(aTask.getTitle())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(aTask.getContent()))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round))
                    .setContentIntent(pendingIntent_toMain)
                    .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Jingle.ogg")))
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            manager.notify(intent.getIntExtra("position",0),notification);
        }
    }
}
