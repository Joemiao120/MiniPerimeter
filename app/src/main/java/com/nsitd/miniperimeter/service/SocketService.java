package com.nsitd.miniperimeter.service;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.vedio.VedioHomeActivity;
import com.nsitd.miniperimeter.bean.DeviceAlarmPushMsgBean;
import com.nsitd.miniperimeter.bean.InvadePushMsgBean;
import com.nsitd.miniperimeter.socket.MyWebSocketClient;
import com.nsitd.miniperimeter.socket.WebSocketListener;
import com.nsitd.miniperimeter.util.CommonAttribute;

import org.java_websocket.drafts.Draft_17;

import java.net.URI;

/**
 * Created by reimu on 16/4/7.
 */
public class SocketService extends Service implements MyWebSocketClient.WebSocketStateListener, WebSocketListener {
    private final String TAG = this.getClass().getSimpleName();
    private MyWebSocketClient client;
    private Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        connectSocket();
    }

    private void connectSocket() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    client = new MyWebSocketClient(new URI(CommonAttribute.getSocketUrl()), new
                            Draft_17(), SocketService.this);
                    client.connectBlocking();
//                    client.send("{'type':'ttt'}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null && client.isConnecting()) try {
            client.closeBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClose() {
        connectSocket();
    }


    @Override
    public void onMessage(Object pushBean) {
        //侵入告警
        if (pushBean instanceof InvadePushMsgBean) {
            InvadePushMsgBean bean = (InvadePushMsgBean) pushBean;
            if (bean != null) {
                //入侵结束
                if (TextUtils.equals(CommonAttribute.INVADE_COMMAND_EVENT_OVER, bean.command)) {
                //入侵开始
                } else if (TextUtils.equals(CommonAttribute.INVADE_COMMAND_ABVIDEOOPEN, bean.command) && TextUtils
                        .equals(CommonAttribute.INVADE_FLAG_EVENT, bean.flag)) {
                    sendNotification();
                }
            }
        } else if (pushBean instanceof DeviceAlarmPushMsgBean) {
        }
        sendNotification();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle("有新报警")
                        .setContentText("点击查看详情");
        Intent resultIntent = new Intent(this, VedioHomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(VedioHomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
