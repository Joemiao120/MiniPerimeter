package com.nsitd.miniperimeter.activity.vedio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.setting.ConfigurationActivity;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.VedioProcess;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;
import com.nsitd.miniperimeter.video.inter.IVedioCallback;

public class VedioFullPlayActivity extends BaseActivity implements View.OnLongClickListener, IVedioCallback {

    private final String TAG = this.getClass().getSimpleName();
    private int channel = 34;
    private VedioProcess vedioProcess;
    private PlaySurfaceView playSurfaceView;
    private AlarmHistory itemAlarmHistory;
    private SurfaceView sv_content;
    private AsyncTask asyncTask;
    //声明进度条对话框
    private ProgressDialog m_pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vedio_full_play);
        super.onCreate(savedInstanceState);

        itemAlarmHistory = ((AlarmHistory) getIntent().getSerializableExtra(CommonAttribute.VedioCameraMessage));
        tv_tilte_bar.setText("实时监控");
        setRightMidView(itemAlarmHistory.channelName);
        sv_content = (SurfaceView) this.findViewById(R.id.sv_content);
        sv_content.setOnLongClickListener(this);

        vedioProcess = VedioProcess.initVedioProcess(this);
        channel = itemAlarmHistory.channel;

        int viewHight = CommonMethod.contentViewHight(this);
        int viewWidth = CommonMethod.contentViewWidth(this);
        LogFactory.i(TAG, "viewHight===" + viewHight);
        playSurfaceView = new PlaySurfaceView(this);
        playSurfaceView.setScreenHight(viewHight);
        playSurfaceView.setScreenWidth(viewWidth);
//        playSurfaceView.setSizeCallback(new PlaySurfaceView.SetSizeCallback() {
//            @Override
//            public int getWidth() {
//                return CommonMethod.contentViewWidth(VedioFullPlayActivity.this) -
//                        (int) VedioFullPlayActivity.this.getResources().getDimension(R.dimen.test);
//            }
//
//            @Override
//            public int getHeight() {
//                return CommonMethod.contentViewHight(VedioFullPlayActivity.this);
//            }
//        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        addContentView(playSurfaceView, params);

        playSurfaceView.setBackgroundColor(getResources().getColor(R.color.gray));
        playSurfaceView.setOnLongClickListener(this);
        playSurfaceView.setCallBack(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showDialog();
        vedioProcess.startVedioPreview(playSurfaceView, channel, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        playSurfaceView.setBackgroundColor(getResources().getColor(R.color.gray));
        vedioProcess.stopVedioPreview(playSurfaceView);

    }

    private void showDialog() {
        //创建ProgressDialog对象
        m_pDialog = new ProgressDialog(this);
        m_pDialog.setIndeterminate(true);
        m_pDialog.show();
        m_pDialog.setContentView(R.layout.dialog_process);
    }

    @Override
    public boolean onLongClick(View v) {
        //界面加载有延迟
        Intent intent = new Intent(this, ConfigurationActivity.class);
        intent.putExtra(CommonAttribute.VedioCameraId, itemAlarmHistory.channelId);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void vedioProcessResultCallback(final int result) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (m_pDialog != null)
                {
                    m_pDialog.dismiss();
                    m_pDialog = null;
                }
                if (result == CommonAttribute.SUCCESS) {
                    playSurfaceView.setBackgroundColor(getResources().getColor(R.color.transparent));
                } else {
                    playSurfaceView.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }
        },0);

    }

    Handler handler = new Handler();

    @Override
    public void handleReceive(Context context) {
        super.handleReceive(context);
        if (!isConnecting){
            finish();
        }
    }
}
