package com.nsitd.miniperimeter.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsitd.miniperimeter.MyApplication;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.vedio.VedioHomeActivity;
import com.nsitd.miniperimeter.bean.DeviceAlarmPushMsgBean;
import com.nsitd.miniperimeter.bean.InvadePushMsgBean;
import com.nsitd.miniperimeter.socket.MyWebSocketClient;
import com.nsitd.miniperimeter.socket.WebSocketListener;
import com.nsitd.miniperimeter.util.AlarmVoicePlayer;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.util.PrefUtils;


/**
 *
 */
public class BaseActivity extends Activity implements WebSocketListener {

    public final String TAG = this.getClass().getSimpleName();
    private static final int RECEIVE_PUSH_MSG = 99;
    private DisplayMetrics displayMetrics;
    private RelativeLayout rightRegionLayout;
    private View rightView;
    private View rightBottomView;
    private View rightMidView;
    private View midIv;
    protected ImageView iv_bg = null;
    protected TextView iv_red_bg = null;
    protected ImageView iv_home;
    protected ImageView iv_red_point;
    protected ImageView iv_back;
    protected TextView tv_tilte_bar;
    protected FrameLayout fl_bg;
    private NetworkChangeReceiver networkChangeReceiver;
    public boolean isConnecting = false;
    public int numberReceiver = 0;

    private ImageView full_play_iv_back;
    private ImageView full_play_iv_home;
    private Handler mBaseHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_PUSH_MSG:
                    if (msg.obj instanceof InvadePushMsgBean) {
                        InvadePushMsgBean bean = (InvadePushMsgBean) msg.obj;
                        onMessageAtUIThread(bean);
                    } else if (msg.obj instanceof DeviceAlarmPushMsgBean) {
                        DeviceAlarmPushMsgBean bean = (DeviceAlarmPushMsgBean) msg.obj;
                        onMessageAtUIThread(bean);
                    }
                    break;
            }
        }
    };

    protected void onMessageAtUIThread(InvadePushMsgBean messagePushBean) {
        showPush(messagePushBean);
        // 播放铃声
        if (messagePushBean.flag.equals("event")) {
            if (messagePushBean.command.equals("abVideoOpen")) {
                AlarmVoicePlayer.getInstance().play();

                PrefUtils.setBoolean(CommonAttribute.PRE_CC_ISALARM, true);
            }
        } else {
            if (messagePushBean.command.equals("event_over")) {
                AlarmVoicePlayer.getInstance().stop();

                PrefUtils.setBoolean(CommonAttribute.PRE_CC_ISALARM, false);
            }
        }
        // 根据状态判断推送信息的状态，保存状态
    }

    protected void onMessageAtUIThread(DeviceAlarmPushMsgBean deviceAlarmPushMsgBean) {
        saveAlarm(deviceAlarmPushMsgBean);
    }

    private void saveAlarm(DeviceAlarmPushMsgBean bean) {
        if (bean != null && bean.content != null && !TextUtils.isEmpty(bean.content.type)) {
            boolean isAlarm = false;
            int preIsAlarm = PrefUtils.getInt(CommonAttribute.PRE_ISALARM, CommonAttribute.HEX_DEFALUT);
            if (TextUtils.equals("1", bean.content.isalarm)) isAlarm = true;
            switch (bean.content.type) {
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_CC:
                    PrefUtils.setBoolean(CommonAttribute.PRE_CC_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_CC;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_CC;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_ALINE:
                    PrefUtils.setBoolean(CommonAttribute.PRE_ALINE_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_ALINE;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_ALINE;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_BLINE:
                    PrefUtils.setBoolean(CommonAttribute.PRE_BLINE_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_BLINE;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_BLINE;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_FN:
                    PrefUtils.setBoolean(CommonAttribute.PRE_FN_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_FN;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_FN;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_VCR:
                    PrefUtils.setBoolean(CommonAttribute.PRE_VCR_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_VCR;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_VCR;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_CAMERA:
                    PrefUtils.setBoolean(CommonAttribute.PRE_CAMERA_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_CAMERA;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_CAMERA;
                    }
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_NC:
                    PrefUtils.setBoolean(CommonAttribute.PRE_NC_ISALARM, isAlarm);
                    if (isAlarm) {
                        preIsAlarm = preIsAlarm | CommonAttribute.HEX_IS_NC;
                    } else {
                        preIsAlarm = preIsAlarm & CommonAttribute.HEX_NOT_NC;
                    }
                    break;
            }
            PrefUtils.setInt(CommonAttribute.PRE_ISALARM, preIsAlarm);
        }
    }


    private void showPush(InvadePushMsgBean bean) {
        if (iv_bg != null && iv_red_bg != null) {
            if (iv_bg.getVisibility() == View.VISIBLE && iv_red_bg.getVisibility() != View.VISIBLE) {
                iv_bg.setVisibility(View.GONE);
                iv_red_bg.setVisibility(View.VISIBLE);
            }
        }

        if (iv_red_point != null) {
            if (iv_red_point.getVisibility() != View.VISIBLE) {
                iv_red_point.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.activity_status_bar_color);// 通知栏所需颜色
        }

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        this.displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LayoutInflater inflater = LayoutInflater.from(this);
        this.rightView = inflater.inflate(R.layout.activity_right_region_view, null);
        rightBottomView = rightView.findViewById(R.id.right_region_bottom_tv);
        rightMidView = rightView.findViewById(R.id.right_region_mid_tv);
        midIv = rightView.findViewById(R.id.right_region_mid_iv);

        initTitleBar();
        initTitleListener();
        MyApplication.addActivity(this);

    }

    private void initTitleBar() {
        iv_bg = (ImageView) this.findViewById(R.id.iv_bg);
        iv_red_bg = (TextView) this.findViewById(R.id.iv_red_bg);
        iv_home = (ImageView) this.findViewById(R.id.iv_home);
        iv_red_point = (ImageView) this.findViewById(R.id.iv_red_point);
        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        tv_tilte_bar = (TextView) this.findViewById(R.id.tv_tilte_bar);
        fl_bg = (FrameLayout) this.findViewById(R.id.fl_bg);
        // 全屏时的右边导航
        full_play_iv_back = (ImageView) rightView.findViewById(R.id.iv_back);
        full_play_iv_home = (ImageView) rightView.findViewById(R.id.iv_home);
    }

    private void initTitleListener() {
        if (fl_bg != null) fl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cotentClick();
            }
        });

        if (iv_home != null) iv_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                homeClick();
            }
        });

        if (iv_back != null) iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backClick();
            }
        });

        if (full_play_iv_home != null)
            full_play_iv_home.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    homeClick();
                }
            });

        if (full_play_iv_back != null)
            full_play_iv_back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    backClick();
                }
            });
    }

    protected void cotentClick() {
        if (iv_bg != null && iv_bg.getVisibility() == View.GONE) iv_bg.setVisibility(View.VISIBLE);


        if (iv_red_bg != null && iv_red_bg.getVisibility() == View.VISIBLE) {
            iv_red_bg.setVisibility(View.GONE);
            if (iv_red_point != null && iv_red_point.getVisibility() == View.VISIBLE) {
                iv_red_point.setVisibility(View.GONE);
            }
            startActivity(new Intent(this, VedioHomeActivity.class));
        } else {
            backClick();
        }

    }

    protected void homeClick() {
        if (iv_bg != null && iv_bg.getVisibility() == View.GONE) iv_bg.setVisibility(View.VISIBLE);
        if (iv_red_point != null && iv_red_point.getVisibility() == View.VISIBLE)
            iv_red_point.setVisibility(View.GONE);
        if (iv_red_bg != null && iv_red_bg.getVisibility() == View.VISIBLE) {
            iv_red_bg.setVisibility(View.GONE);
        }
        startActivity(new Intent(this, VedioHomeActivity.class));
    }

    protected void backClick() {
        this.finish();
    }

    /**
     * 显示 右边导航
     */
    public void showRightRegionView() {
        int viewHight = CommonMethod.contentViewHight(this);
        int topMa = CommonMethod.getStatusBarHeight(this);
//        int viewWidth = (int) ((viewHight / 9) * 16);//根据 全屏显示计算右边区域的width
        int viewWidth = CommonMethod.contentViewWidth(this) - (int) this.getResources().getDimension(R.dimen.test);

        CommonAttribute.ShowContentViewWidth = viewWidth;

        rightRegionLayout = new RelativeLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                (displayMetrics.widthPixels - viewWidth),
                (int) getResources().getDimension(R.dimen.test),
                FrameLayout.LayoutParams.MATCH_PARENT);

        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.topMargin = topMa;

        removeRightRegionView();
        rightRegionLayout.addView(rightView, params);
        addContentView(rightRegionLayout, params);
    }

    /**
     * 设置 右边区域的 底部文字
     *
     * @param content
     */
    public void setRightRegionBottomViewContent(String content) {

        ((TextView) rightBottomView).setText(VerticalText(content));
    }

    /**
     * 设置 中间区域的内容
     *
     * @param content
     */
    public void setRightRegionMidviewContent(String content) {
        ((TextView) rightMidView).setText(VerticalText(content));
    }

    private String VerticalText(String content) {
        int m = content.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m; i++) {
            CharSequence index = content.subSequence(i, i + 1);
            sb.append(index);
            if (i < m - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 设置右边区域 中间文字
     */
    public void setRightMidView(String content) {
        this.iv_red_bg.setText(VerticalText(content));
        this.iv_red_bg.setVisibility(View.VISIBLE);
    }


    /**
     * 设置右边区域 中间图片
     *
     * @param drawableId
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightRegionMidIv(int drawableId) {
        midIv.setBackground(getResources().getDrawable(drawableId));
    }


    /**
     * 移除 右边导航
     */
    public void removeRightRegionView() {
        if (rightRegionLayout.getParent() != null) {
            ((ViewGroup) rightRegionLayout.getParent()).removeView(rightRegionLayout);
        }
    }


    public <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }

    protected void showLongToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
    }

    protected void showShortToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyWebSocketClient.setmListener(this);

        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        LogFactory.i(TAG, this.getClass().getName()+"== onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onMessage(Object pushBean) {

        Log.i(TAG, pushBean.toString());
        if (pushBean != null) {
            Message message = Message.obtain();
            message.obj = pushBean;
            message.what = RECEIVE_PUSH_MSG;
            mBaseHandler.sendMessage(message);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.closeActivity(this.getClass());

    }

    public void handleReceive(Context context) {
        //获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo info = connManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    isConnecting = true;
                    return;
                }
            }
        }
        isConnecting = false;
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 处理事件
            handleReceive(context);
        }
    }
}
