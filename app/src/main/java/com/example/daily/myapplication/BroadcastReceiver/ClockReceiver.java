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

import com.example.daily.myapplication.MainActivity;
import com.example.daily.myapplication.R;
import com.example.daily.myapplication.entityClass.Task;

import java.io.File;

public class ClockReceiver extends BroadcastReceiver {
    private File mediaFile;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("test");
        Bundle test = intent.getExtras();
        Task thisTask = (Task) bundle.getSerializable("aTask");
        File mediaFile = (File) bundle.getSerializable("mediaFile");

        Intent intentToMain = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent_toMain = PendingIntent.getActivity(context, 0,
                intentToMain, 0);
        NotificationManager
                manager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(thisTask.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(thisTask.getContent()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap
                        .ic_launcher_round))
                .setContentIntent(pendingIntent_toMain)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        if (mediaFile != null) {
            builder.setSound(Uri.fromFile(mediaFile));
        }
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_INSISTENT;
        manager.notify(thisTask.getHashCode(), notification);

    }
}
