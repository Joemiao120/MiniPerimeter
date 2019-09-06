package com.nsitd.miniperimeter.activity.vedio;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.HistoryAlarmActivity;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.activity.setting.SelectDateActivity;
import com.nsitd.miniperimeter.activity.setting.SystemSettingActivity;
import com.nsitd.miniperimeter.bean.InvadePushMsgBean;
import com.nsitd.miniperimeter.bean.ViewInitBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.service.MessageService;
import com.nsitd.miniperimeter.service.SocketService;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.VedioProcess;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;

import java.util.ArrayList;
import java.util.List;

public class VedioHomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private VedioProcess vedioProcess = null;
    private VedioGridViewAdapter vedioGridViewAdapter = null;


    private int delaryTimer = 1000;
    private Handler handler = new Handler();
    private View setRedIv;
    private View titleRedIv;
    private View sectorIv;
    private List<AlarmHistory> cameraList = new ArrayList<>();
    private int numColumns = 3;
    private int totalItmeNum = 6;
    private boolean isSector = false;//是否布防
    private GridView gridView;
    private AlarmHistory itemAlarmHistory;
    private Intent serviceIntent;
    private Intent messageServiceIntent;
    private HttpRequest httpRequest;
    private long mExitTime = 0l;


    private boolean isActivityShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_home);


        vedioProcess = VedioProcess.initVedioProcess(this);
        vedioProcess.initSDK();
        httpRequest = HttpRequest.getInstance(this);

        initfindId();
        showVedioGridView();
        vedioGridViewAdapter();
        obtainRecordData();
        serviceIntent = new Intent(this, SocketService.class);
        startService(serviceIntent);
        messageServiceIntent = new Intent(this, MessageService.class);
        startService(messageServiceIntent);
        CommonAttribute.VedioHomeLoadNumber = 0;
    }

    private void obtainRecordData() {

        HttpListenerWrap.IHttpListenerWrapCallback<ViewInitBean> callback = new HttpListenerWrap.IHttpListenerWrapCallback<ViewInitBean>() {
            @Override
            public void finished(final HttpListenerState requestSate, final String resultSate, String message, final ViewInitBean
                    viewInitBean) {
                final List<ViewInitBean.ManagerResource> managerResources = viewInitBean.content.managerResourceList;
                if (managerResources.size() > 0) {
                    final ViewInitBean.ManagerResource managerResource = managerResources.get(0);
                    if (requestSate == HttpListenerState.SERVER_FINISH) {
                        AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] params) {
                                boolean result = false;
                                if (CommonAttribute.RESULTSATE_SUCCESS.equals(resultSate)) {
                                    result = vedioProcess.login(managerResource.managerResourceIp, managerResource.managerResourceLoginPort, managerResource.managerResourceLoginName, managerResource.managerResourceLoginPwd);

                                }

                                return result;
                            }

                            @Override
                            protected void onPostExecute(Object params) {
                                super.onPostExecute(params);
                                boolean result = ((boolean) params);
                                LogFactory.i(TAG, "login result ===" + result);
                                CommonAttribute.CameraList.clear();
                                if (result) {
//                                    showShortToast("登陆录像机成功");
                                    fillRecordDatalist(managerResource);
                                } else {
//                                    showShortToast("登陆录像机失败");
                                }
                            }
                        };
                        asyncTask.execute();
                    }
                }

            }
        };
        httpRequest.viewInit(callback);
    }

    private void fillRecordDatalist(ViewInitBean.ManagerResource managerResource)
    {
        for (ViewInitBean.ViewResource viewResource : managerResource.resourceList) {

            AlarmHistory alarmHistory = new AlarmHistory();
            alarmHistory.channel = 32 + Integer.valueOf(viewResource.managerResourceChannel);//需要配置 以32为基数
            alarmHistory.channelName = viewResource.resourceName;
            alarmHistory.channelId = viewResource.resourceId;
            cameraList.add(alarmHistory);

            vedioGridViewAdapter.setLogText(cameraList.size() - 1, "正在登陆摄像头...");
        }

        // 保存成为全局变量
        CommonAttribute.CameraList = cameraList;

        vedioGridViewAdapter.setCameraList(cameraList);
        vedioGridViewAdapter.notifyDataSetChanged();
        if (isActivityShow)//规避app 异常重新获取数据
        {
            vedioGridViewAdapter.startPreview(true);
        }
    }



    private void initfindId() {
        this.setRedIv = $(R.id.home_set_red);
//        this.titleRedIv = $(R.id.home_monitor_red);
        this.sectorIv = $(R.id.home_sector_iv);

        View setIv = $(R.id.home_set_layout);
        View playBackIv = $(R.id.home_history_playback_iv);
        View alarmIv = $(R.id.home_history_alarm_iv);

        setIv.setOnClickListener(this);
        playBackIv.setOnClickListener(this);
        alarmIv.setOnClickListener(this);
        sectorIv.setOnClickListener(this);

        showSectorBg();
    }

    private void showVedioGridView() {
        ViewGroup viewGroup = ((ViewGroup) findViewById(R.id.content_relativelayout));

        this.gridView = new GridView(this);
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.activity_vedio_gridview_horizontalSpacing));
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.activity_vedio_gridview_verticalSpacing));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        gridView.setLayoutParams(params);
        gridView.setNumColumns(numColumns);

        viewGroup.addView(gridView);
    }

    private void vedioGridViewAdapter() {
        vedioGridViewAdapter = new VedioGridViewAdapter(this, numColumns, totalItmeNum, 0);
        gridView.setAdapter(vedioGridViewAdapter);
        gridView.setOnItemClickListener(this);
    }

    private int getLocation(int channel) {

        for (int i = 0; i < cameraList.size(); i++) {
            if (channel == cameraList.get(i).channel) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void onMessageAtUIThread(InvadePushMsgBean messagePushBean) {
        super.onMessageAtUIThread(messagePushBean);

        // 消息预警
        int i = getLocation(32 + Integer.valueOf(messagePushBean.channel));
        if (i != -1) {
            cameraList.get(i).alarmTag = 1;
            vedioGridViewAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home_set_layout://设置
                startActivity(new Intent(this, SystemSettingActivity.class));
                break;
            case R.id.home_history_playback_iv://历史回放
                startActivity(new Intent(this, SelectDateActivity.class));
                break;
            case R.id.home_history_alarm_iv://告警
                startActivity(new Intent(this, HistoryAlarmActivity.class));
                break;
            case R.id.home_sector_iv://布防
                isSector = !isSector;
                showSectorBg();
                break;
        }

    }

    private void showSectorBg() {
        if (isSector) {
            sectorIv.setBackgroundResource(R.drawable.home_sector_off);
        } else {
            sectorIv.setBackgroundResource(R.drawable.home_sector_on);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < cameraList.size()) {
            if (isConnecting) {
                itemAlarmHistory = cameraList.get(position);
                Intent intent = new Intent();
                intent.setAction(CommonAttribute.VedioFullViewPlayAction);
                intent.putExtra(CommonAttribute.VedioCameraMessage, itemAlarmHistory);
                startActivity(intent);
            } else {
                CommonMethod.showTextToast(this, "网络异常，请检查网络");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent == null)
            serviceIntent = new Intent(this, SocketService.class);
        stopService(serviceIntent);

        if (messageServiceIntent == null)
            messageServiceIntent = new Intent(this, MessageService.class);
        stopService(messageServiceIntent);

    }

    @Override
    public void handleReceive(Context context) {
        super.handleReceive(context);
        if (numberReceiver > 0 && cameraList.size() != 0) {
            LogFactory.i(TAG,"WIFI handleReceive");
            vedioGridViewAdapter.startPreview(false);
            vedioGridViewAdapter.startPreview(true);
        }
        numberReceiver++;

        setNetworkShow();
    }

    private void setNetworkShow() {
        if (!isConnecting) {
            for (int i = 0; i < cameraList.size(); i++) {
                vedioGridViewAdapter.setLogText(i, "无网络");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraList.size() != 0) {
            vedioGridViewAdapter.startPreview(true);
        }
        setNetworkShow();
        isActivityShow = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        vedioGridViewAdapter.startPreview(false);
        isActivityShow = false;

        numberReceiver = 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                boolean result = vedioProcess.logout();
                LogFactory.e(TAG, "result" + result);
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
