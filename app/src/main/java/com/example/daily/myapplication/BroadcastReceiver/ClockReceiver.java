package com.example.daily.myapplication.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.daily.myapplication.DBHelper.DBHelper;
import com.example.daily.myapplication.MainActivity;
import com.example.daily.myapplication.R;
import com.example.daily.myapplication.entityClass.Task;

import java.io.File;

public class ClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceiveIfChan", "com.example.daily.myapplication->onReceive: start");
        if(intent.getAction().equals("com.example.daily.myapplication.ACTION_SEND")) {
            File mediaFile = (File) bundle.getSerializable("mediaFile");

            Intent intent_toMain = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent_toMain = PendingIntent.getActivity(context,0,intent_toMain,0);

            Task aTask = (Task) bundle.getSerializable("aTask");
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(aTask.getTitle())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(aTask.getContent()))
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round))
                    .setContentIntent(pendingIntent_toMain)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            if(mediaFile != null) {
                builder.setSound(Uri.fromFile(mediaFile));
            }
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_INSISTENT;
            manager.notify(bundle.getInt("position",0),notification);
            Log.d("onReceiveIfChan","com.example.daily.myapplication->onReceive: succ");

        }


    }
}
