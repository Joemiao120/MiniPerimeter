package com.nsitd.miniperimeter.activity.vedio.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.activity.vedio.VedioGridViewAdapter;
import com.nsitd.miniperimeter.bean.DateBean;
import com.nsitd.miniperimeter.bean.EventBusBean;
import com.nsitd.miniperimeter.util.CalendarUtil;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.bean.PlayProgressBean;
import com.nsitd.miniperimeter.video.custom_view.VedioPlayControlView;

import org.MediaPlayer.PlayM4.Player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 告警 历史
 */
public class VedioAlarmHistoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private VedioGridViewAdapter vedioGridViewAdapter = null;
    private int[] startTime = new int[6];
    private int[] endTime = new int[6];

    private DateBean startDate;
    private DateBean endDate;
    private boolean isAlarm;
    private VedioPlayControlView vedioPlayControlView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    vedioPlayControlView.invalidateView();
                    break;
            }
        }
    };
    private List<AlarmHistory> cameraList = new ArrayList<>();
    private int numColumns = 3;
    private int totalItmeNum = 6;
    private GridView gridView;
    private List<PlayProgressBean> seekbarInterval;//时间单位 毫秒
    private boolean clickFlag = false;

    private boolean isFristLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm_history);
        super.onCreate(savedInstanceState);

        tv_tilte_bar.setText("历史告警");
        cameraList.clear();
        initIntent();
        showVedioGridView();
        vedioGridViewAdapter();
        showPlayControlView();

        vedioGridViewAdapter.setVedioPlayControlView(vedioPlayControlView);
        if (cameraList.size() > 0) {
            cameraList.get(0).currentTime = cameraList.get(0).startTime;
            cameraList.get(0).playProgressBeans = seekbarInterval;
        }
        isFristLoad = true;
    }

    private void initIntent() {
        startDate = (DateBean) getIntent().getSerializableExtra("startTime");
        endDate = (DateBean) getIntent().getSerializableExtra("endTime");
        isAlarm = getIntent().getBooleanExtra("isAlarm", false);

        if (isAlarm) {
            startDate = CalendarUtil.getDate(startDate, Calendar.MINUTE, -1);
            endDate = CalendarUtil.getDate(endDate, Calendar.MINUTE, 1);
        }
        setData(startDate, startTime);
        setData(endDate, endTime);
    }

    private void showVedioGridView() {
        ViewGroup viewGroup = ((ViewGroup) findViewById(R.id.vedio_content_layout));

        int viewHight = CommonMethod.contentViewHight(this);
//        int viewWidth = (int) ((viewHight / 9) * 16);//根据 全屏显示计算右边区域的width
        int viewWidth = CommonMethod.contentViewWidth(this);

        this.gridView = new GridView(this);
        // 设置间隔
        gridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.activity_vedio_gridview_horizontalSpacing));
        gridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.activity_vedio_gridview_verticalSpacing));

        // 设置内容的参数
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                (viewWidth - (int) getResources().getDimension(R.dimen.test)),
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;

        gridView.setLayoutParams(params);
        gridView.setNumColumns(numColumns);
        viewGroup.addView(gridView);
    }

    private void vedioGridViewAdapter() {
        checkAlarmPlayMessage();
        vedioGridViewAdapter = new VedioGridViewAdapter(this, numColumns, totalItmeNum, 1);
        vedioGridViewAdapter.setCameraList(cameraList);
        gridView.setAdapter(vedioGridViewAdapter);
        gridView.setOnItemClickListener(this);
    }

    /**
     * 显示 play control view
     */
    private void showPlayControlView() {
        vedioPlayControlView = new VedioPlayControlView(this);

        vedioPlayControlView.setShowTimeView(View.INVISIBLE);
        vedioPlayControlView.setBackgroundColor(R.color.activity_status_bar_color);
        vedioPlayControlView.setSeekbarThumb(R.drawable.play_alarm_progress_thumb);
//        vedioPlayControlView.setControlViewLeftMargin((int) getResources().getDimension(R.dimen.activity_vedio_gridview_horizontalSpacing));
//        vedioPlayControlView.setControlViewRightMargin((int) getResources().getDimension(R.dimen.activity_vedio_gridview_horizontalSpacing));
        vedioPlayControlView.showPlayControlView();

        setSeekStartEndTime();
    }

    private void setSeekStartEndTime() {
        seekbarInterval = new ArrayList<>();

        PlayProgressBean playProgressBean = new PlayProgressBean();
        playProgressBean.interval = 3 * 1000;
        playProgressBean.type = CommonAttribute.Play_seekbar_start_endType;
        seekbarInterval.add(playProgressBean);
        PlayProgressBean playProgressBeanF = new PlayProgressBean();
        playProgressBeanF.interval = 4 * 1000;
        playProgressBeanF.type = CommonAttribute.Play_seekbar_alarmType;
        seekbarInterval.add(playProgressBeanF);
        PlayProgressBean playProgressBeanT = new PlayProgressBean();
        playProgressBeanT.interval = 3 * 1000;
        playProgressBeanT.type = CommonAttribute.Play_seekbar_start_endType;
        seekbarInterval.add(playProgressBeanT);
    }

    int position;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!clickFlag && position < cameraList.size()) {
            if (isConnecting) {

                vedioGridViewAdapter.setWaitingView(true, position);
                this.position = position;

                clickFlag = true;
                AlarmHistory alarmHistory = cameraList.get(position);
                // 请稍后点击
                Player.MPSystemTime mpSystemTime = new Player.MPSystemTime();
                vedioGridViewAdapter.getSystemTime(position, mpSystemTime);
                alarmHistory.currentTime = new int[6];
                alarmHistory.currentTime[0] = mpSystemTime.year;
                alarmHistory.currentTime[1] = mpSystemTime.month;
                alarmHistory.currentTime[2] = mpSystemTime.day;
                alarmHistory.currentTime[3] = mpSystemTime.hour;
                alarmHistory.currentTime[4] = mpSystemTime.min;
                alarmHistory.currentTime[5] = mpSystemTime.sec;

                Intent intent = new Intent(this, VedioAlarmHistoryFullPlayActivity.class);
                intent.putExtra(CommonAttribute.VedioCameraMessage, alarmHistory);
                startActivityForResult(intent, 0);
            } else {
                CommonMethod.showTextToast(this, "网络异常，请检查网络");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Calendar calendar = (Calendar) data.getSerializableExtra("CurrentTime");
            int[] currentTime = new int[6];
            currentTime[0] = calendar.get(Calendar.YEAR);
            currentTime[1] = calendar.get(Calendar.MONTH);
            currentTime[2] = calendar.get(Calendar.DAY_OF_MONTH);
            currentTime[3] = calendar.get(Calendar.HOUR_OF_DAY);
            currentTime[4] = calendar.get(Calendar.MINUTE);
            currentTime[5] = calendar.get(Calendar.SECOND);

            cameraList.get(0).currentTime = currentTime;
            vedioGridViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleReceive(Context context) {
        super.handleReceive(context);
        // 重新填充数据
        if (numberReceiver > 0) {
            vedioPlayControlView.setViewUnclick(isConnecting);
        }
        numberReceiver++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        clickFlag = false;

        //play control
        if (isFristLoad) //第一次界面显示出来时，才能加载视频
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPlayback();
                }
            }, 1000);
            isFristLoad = false;
        } else {

            for (int i = 0; i < cameraList.size(); i++) {
                vedioGridViewAdapter.setWaitingView(true, i);
            }

            startPlayback();
        }
    }

    private void startPlayback() {
        if (cameraList.size() > 0) {
            vedioGridViewAdapter.setPlaybackSurface(cameraList.get(0));
            vedioGridViewAdapter.startPlayback(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vedioGridViewAdapter.startPlayback(false);
        vedioPlayControlView.onDestory();
        numberReceiver = 0;

        vedioGridViewAdapter.setWaitingView(false, position);

        EventBus.getDefault().unregister(this);
    }

    /**
     * 根据时间查询历史回放信息
     */
    private void checkAlarmPlayMessage() {
        if (CommonAttribute.CameraList != null && CommonAttribute.CameraList.size() > 0) {
            for (int i = 0; i < CommonAttribute.CameraList.size(); i++) {
                AlarmHistory alarmHistory = CommonAttribute.CameraList.get(i);
                alarmHistory.startTime = startTime;
                alarmHistory.endTime = endTime;
                cameraList.add(alarmHistory);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserEvent(EventBusBean event) {
        if (event.getMsg().equals("start")) {
            vedioGridViewAdapter.setWaitingView(false, 0);
        }
    }

    private void setData(DateBean data, int[] dataTime) {
        dataTime[0] = data.year;
        dataTime[1] = data.month;
        dataTime[2] = data.day;
        dataTime[3] = data.hour;
        dataTime[4] = data.minute;
        dataTime[5] = data.second;
    }


}
