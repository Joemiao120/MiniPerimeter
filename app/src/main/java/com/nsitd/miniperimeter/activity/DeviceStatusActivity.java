package com.nsitd.miniperimeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.DeviceAlarmPushMsgBean;
import com.nsitd.miniperimeter.bean.FnCountInfoBean;
import com.nsitd.miniperimeter.http.HttpListenerState;
import com.nsitd.miniperimeter.http.HttpListenerWrap;
import com.nsitd.miniperimeter.http.HttpRequest;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.PrefUtils;

/**
 * Created by reimu on 16/4/19.
 */
public class DeviceStatusActivity extends BaseActivity implements View.OnClickListener {

    private Button bt_device_nc;
    private Button bt_device_cc;
    private Button bt_device_aline;
    private Button bt_device_bline;
    private ImageView iv_device_cc_red_point;
    private ImageView iv_device_nc_red_point;
    private ImageView iv_device_aline_red_point;
    private ImageView iv_device_bline_red_point;
    private HttpRequest mHttpRequest;
    private FnCountInfoBean.FnCountInfo aFnInfo;
    private FnCountInfoBean.FnCountInfo bFnInfo;
    private boolean ccIsAlarm;
    private boolean alineIsAlarm;
    private boolean blineIsAlarm;
    private boolean isAlarm;
    private boolean ncIsAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_status);
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        ccIsAlarm = PrefUtils.getBoolean(CommonAttribute.PRE_CC_ISALARM, false);
        alineIsAlarm = PrefUtils.getBoolean(CommonAttribute.PRE_ALINE_ISALARM, false);
        blineIsAlarm = PrefUtils.getBoolean(CommonAttribute.PRE_BLINE_ISALARM, false);
        ncIsAlarm = PrefUtils.getBoolean(CommonAttribute.PRE_NC_ISALARM, false);
        iv_device_cc_red_point.setVisibility(ccIsAlarm ? View.VISIBLE : View.GONE);
        iv_device_aline_red_point.setVisibility(alineIsAlarm ? View.VISIBLE : View.GONE);
        iv_device_bline_red_point.setVisibility(blineIsAlarm ? View.VISIBLE : View.GONE);
        iv_device_nc_red_point.setVisibility(ncIsAlarm ? View.VISIBLE : View.GONE);

        mHttpRequest = HttpRequest.getInstance(this);
        mHttpRequest.obtainFnCountInfo(new HttpListenerWrap.IHttpListenerWrapCallback<FnCountInfoBean>() {
            @Override
            public void finished(HttpListenerState requestSate, String resultSate, String message, FnCountInfoBean
                    fnCountInfoBean) {
                if (requestSate == HttpListenerState.SERVER_FINISH && CommonAttribute.RESULTSATE_SUCCESS.equals
                        (resultSate) && fnCountInfoBean != null && fnCountInfoBean.content != null &&
                        fnCountInfoBean.content.fncountlists.size() > 0) {
                    for (FnCountInfoBean.FnCountInfo fnCountInfo : fnCountInfoBean.content.fncountlists) {
                        if (fnCountInfo != null && "A线".equals(fnCountInfo.port)) {
                            bt_device_aline.setText("A线(" + fnCountInfo.fnruncount + "/" + fnCountInfo.fnbasecount +
                                    ")");
                            aFnInfo = fnCountInfo;
                        }
                        if (fnCountInfo != null && "B线".equals(fnCountInfo.port)) {
                            bt_device_bline.setText("B线(" + fnCountInfo.fnruncount + "/" + fnCountInfo.fnbasecount +
                                    ")");
                            bFnInfo = fnCountInfo;
                        }
                    }
                }
            }
        });
    }

    private void initListener() {
        bt_device_nc.setOnClickListener(this);
        bt_device_cc.setOnClickListener(this);
        bt_device_aline.setOnClickListener(this);
        bt_device_bline.setOnClickListener(this);
    }

    private void initView() {
        bt_device_nc = (Button) this.findViewById(R.id.bt_device_nc);
        bt_device_cc = (Button) this.findViewById(R.id.bt_device_cc);
        bt_device_aline = (Button) this.findViewById(R.id.bt_device_aline);
        bt_device_bline = (Button) this.findViewById(R.id.bt_device_bline);
        iv_device_cc_red_point = (ImageView) this.findViewById(R.id.iv_device_cc_red_point);
        iv_device_nc_red_point = (ImageView) this.findViewById(R.id.iv_device_nc_red_point);
        iv_device_aline_red_point = (ImageView) this.findViewById(R.id.iv_device_aline_red_point);
        iv_device_bline_red_point = (ImageView) this.findViewById(R.id.iv_device_bline_red_point);
        tv_tilte_bar.setText("设备状态");
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_device_nc:
                if (ncIsAlarm) {
                    ncIsAlarm = false;
                    iv_device_nc_red_point.setVisibility(View.GONE);
                    PrefUtils.setBoolean(CommonAttribute.PRE_NC_ISALARM, false);
                }
                break;
            case R.id.bt_device_cc:
                if (ccIsAlarm) {
                    ccIsAlarm = false;
                    iv_device_cc_red_point.setVisibility(View.GONE);
                    PrefUtils.setBoolean(CommonAttribute.PRE_CC_ISALARM, false);
                }
                break;
            case R.id.bt_device_aline:
                if (alineIsAlarm) {
                    alineIsAlarm = false;
                    iv_device_aline_red_point.setVisibility(View.GONE);
                    PrefUtils.setBoolean(CommonAttribute.PRE_ALINE_ISALARM, false);
                }
                if (aFnInfo != null && !TextUtils.isEmpty(aFnInfo.fnerrorcount)) {
                    int errorCount = Integer.parseInt(aFnInfo.fnerrorcount);
                    if (errorCount > 0) {
                        intent = new Intent(this, LineFaultActivity.class);
                        intent.putExtra(CommonAttribute.LINE_TYPE, CommonAttribute.LINE_TYPE_A);
                        intent.putExtra("data", aFnInfo);
                        startActivity(intent);
                    }
                }

                break;
            case R.id.bt_device_bline:
                if (blineIsAlarm) {
                    blineIsAlarm = false;
                    iv_device_bline_red_point.setVisibility(View.GONE);
                    PrefUtils.setBoolean(CommonAttribute.PRE_BLINE_ISALARM, false);
                }
                if (bFnInfo != null && !TextUtils.isEmpty(bFnInfo.fnerrorcount)) {
                    int errorCount = Integer.parseInt(bFnInfo.fnerrorcount);
                    if (errorCount > 0) {
                        intent = new Intent(this, LineFaultActivity.class);
                        intent.putExtra(CommonAttribute.LINE_TYPE, CommonAttribute.LINE_TYPE_B);
                        intent.putExtra("data", bFnInfo);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    @Override
    protected void onMessageAtUIThread(DeviceAlarmPushMsgBean deviceAlarmPushMsgBean) {
        super.onMessageAtUIThread(deviceAlarmPushMsgBean);
        showAlarm(deviceAlarmPushMsgBean);
    }

    private void showAlarm(DeviceAlarmPushMsgBean bean) {
        if (bean != null && bean.content != null && !TextUtils.isEmpty(bean.content.type)) {
            isAlarm = false;
            if (TextUtils.equals("1", bean.content.isalarm)) isAlarm = true;
            switch (bean.content.type) {
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_CC:
                    iv_device_cc_red_point.setVisibility(isAlarm ? View.VISIBLE : View.GONE);
                    ccIsAlarm = isAlarm;
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_ALINE:
                    iv_device_aline_red_point.setVisibility(isAlarm ? View.VISIBLE : View.GONE);
                    alineIsAlarm = isAlarm;
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_BLINE:
                    iv_device_bline_red_point.setVisibility(isAlarm ? View.VISIBLE : View.GONE);
                    blineIsAlarm = isAlarm;
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_FN:
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_VCR:
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_CAMERA:
                    break;
                case CommonAttribute.DEVICE_ALARM_PUSH_TYPE_NC:
                    iv_device_nc_red_point.setVisibility(isAlarm ? View.VISIBLE : View.GONE);
                    ncIsAlarm = isAlarm;
                    break;

            }
        }
    }
}

