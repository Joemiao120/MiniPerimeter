package com.nsitd.miniperimeter.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.vedio.VedioHomeActivity;
import com.nsitd.miniperimeter.socket.MyWebSocketClient;
import com.nsitd.miniperimeter.socket.WebSocketListener;


/**
 * Created by mac on 16/4/22.
 */
public class MessageService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onMessage(Object pushBean) {
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.icon)
//                        .setContentTitle("有新报警")
//                        .setContentText("点击查看详情");
//        Intent resultIntent = new Intent(this, VedioHomeActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(VedioHomeActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1, mBuilder.build());
//    }
}
