package com.nsitd.miniperimeter.socket;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.nsitd.miniperimeter.bean.DeviceAlarmPushMsgBean;
import com.nsitd.miniperimeter.bean.InvadePushMsgBean;
import com.nsitd.miniperimeter.service.SocketService;
import com.nsitd.miniperimeter.util.CommonAttribute;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MyWebSocketClient extends WebSocketClient {
    private static WebSocketListener mListener;
    private static List<String> messages = new ArrayList<>();
    private boolean isClose = false;
    private WebSocketStateListener mStateListener;
    private WebSocketListener mWebSocketListener;
    private static long preTime = 0l;
    //重连次数
    public static int reconnectCount = 0;
    private int sleepTime;
    private static Gson gson;

    public MyWebSocketClient(URI serverUri, Draft draft, SocketService socketService) {
        super(serverUri, draft);
        this.mStateListener = socketService;
        this.mWebSocketListener = socketService;
        gson = new Gson();
        preTime = 0l;
    }

    public MyWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
        gson = new Gson();
        preTime = 0l;
    }

    public MyWebSocketClient(URI serverURI) {
        super(serverURI);
        gson = new Gson();
        preTime = 0l;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        if (TextUtils.isEmpty(message)) return;
        Object pushBean = json2MessageObject(message);
        if (mWebSocketListener != null) mWebSocketListener.onMessage(pushBean);
        if (mListener != null) {
            if (pushBean != null) {
                mListener.onMessage(pushBean);
            }

        } else messages.add(message);
    }

    private static Object json2MessageObject(String message) {
        if (message.contains(CommonAttribute.METHOD_DEVICEALARMMSG)) {
            DeviceAlarmPushMsgBean bean = gson.fromJson(message, DeviceAlarmPushMsgBean.class);
            return bean;
        } else {
            InvadePushMsgBean pushBean = gson.fromJson(message, InvadePushMsgBean.class);
            return pushBean;
        }
    }

    @Override
    public void onFragment(Framedata fragment) {
        System.out.println("received fragment: " + new String(fragment.getPayloadData().array()));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
        //如果不是主动关闭则重新启动
        if (!isClose) {
            long time = System.currentTimeMillis();
            if (reconnectCount < 10) {
                sleepTime = 10000;
            } else {
                sleepTime = 60 * 1000;
            }
            Log.i("MyWebSocketClient", "MyWebSocketClient:sleepTime:" + sleepTime + "time:" + time + ",(time - preTime):" + (time - preTime) + ",preTime:" + preTime);
            if (preTime == 0L) preTime = time;
            //如果不是第一次中断.并且两次中断时间小于1s,并且重连次数小于10.则先sleep
            if ((preTime != 0l && (time - preTime) < sleepTime)) {
                SystemClock.sleep(sleepTime - (time - preTime));
            }
            preTime = time;
            if (mStateListener != null) {
                mStateListener.onClose();
                reconnectCount++;
            }
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }


    public static void setmListener(WebSocketListener mListener) {
        MyWebSocketClient.mListener = mListener;
        if (messages.size() > 0) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                String message = messages.get(i);
                Object bean = json2MessageObject(message);
                if (bean != null)
                    mListener.onMessage(bean);
                messages.remove(i);
            }
        }
    }

    @Override
    public void closeBlocking() throws InterruptedException {
        isClose = true;
        super.closeBlocking();
    }

    public interface WebSocketStateListener {
        void onClose();
    }
}
