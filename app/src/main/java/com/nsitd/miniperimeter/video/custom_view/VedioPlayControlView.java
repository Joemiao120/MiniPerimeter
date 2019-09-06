package com.nsitd.miniperimeter.video.custom_view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hikvision.netsdk.NET_DVR_TIME;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.vedio.alarm.VedioAlarmHistoryFullPlayActivity;
import com.nsitd.miniperimeter.bean.EventBusBean;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.VedioProcess;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.bean.PlayProgressBean;

import org.MediaPlayer.PlayM4.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by happy on 16/3/23.
 */
public class VedioPlayControlView implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final String TAG = this.getClass().getSimpleName();

    private Activity context;
    private View playControlView;
    private View startPause;
    private View vedioFast;
    private View vedioSlow;
    private TextView currentTime;
    private TextView totalTimeTV;
    private SeekBar progressSeekbar;
    private boolean isPlay = false;
    private int playRate = 0;
    private ViewGroup showTimeView = null;
    private ViewGroup controlLayout = null;

    private int leftMargin = 0;
    private int rigthMargin = 0;
    private View toEnd;
    private View toStart;
    private VedioProcess vedioProcess;

    private List<PlaySurfaceView> surfaceViewList;

    private final int playPause = 1;
    private final int playRecovery = 0;
    //miaoyc
    private NET_DVR_TIME originalNet_dvr_time = new NET_DVR_TIME();
    private NET_DVR_TIME startNet_dvr_time = new NET_DVR_TIME();
    private NET_DVR_TIME endNet_dvr_time = new NET_DVR_TIME();
    private NET_DVR_TIME current_dvr_time;
    private NET_DVR_TIME buf_dvr_time = new NET_DVR_TIME();
    private int playChannel = -1;
    private SimpleDateFormat simpleDateFormat;
    private double timerProportion = 0.0;
    private Calendar startCalendar;
    private Calendar endCalendar;
    private Calendar temCalendar;
    private int delayTime = 400;
    private PlaySurfaceView currentPlaySurfaceView;
    private long totalDisparity = 0; //单位 毫秒
    private Player.MPSystemTime mpSystemTime;
    private long preDisTmer = 0;
    private int fraction = 100000;
    private volatile boolean isStopRun = false;
    private int disNumber = 0;

    Handler handler = new Handler();
    private View pauseStartLayout;
    private int viewWithd = 0;
    private ViewGroup progressLayout = null;

    private TextView slowrateTextView;
    private TextView fastrateTextView;

    public VedioPlayControlView(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        playControlView = inflater.inflate(R.layout.vedio_play_control, null);

        progressSeekbar = ((SeekBar) playControlView.findViewById(R.id.vedio_play_progress_seekbar));
        progressSeekbar.setOnSeekBarChangeListener(this);
        progressLayout = ((ViewGroup) playControlView.findViewById(R.id.play_progress_seekbar_layout));


        this.currentTime = ((TextView) playControlView.findViewById(R.id.vedio_play_current_time_tv));
        this.totalTimeTV = ((TextView) playControlView.findViewById(R.id.vedio_play_total_time_tv));
        this.startPause = playControlView.findViewById(R.id.vedio_play_start_pause_bt);
        this.vedioFast = playControlView.findViewById(R.id.vedio_play_fast_bt);
        this.vedioSlow = playControlView.findViewById(R.id.vedio_play_slow_bt);
        toEnd = playControlView.findViewById(R.id.vedio_play_to_end);
        toStart = playControlView.findViewById(R.id.vedio_play_to_start);
        pauseStartLayout = playControlView.findViewById(R.id.vedio_play_start_pause_layout);

        slowrateTextView = (TextView) playControlView.findViewById(R.id.slow_show_rate);
        fastrateTextView = (TextView) playControlView.findViewById(R.id.fast_show_rate);

        showTimeView = ((ViewGroup) playControlView.findViewById(R.id.showTime_layout));
        controlLayout = ((ViewGroup) playControlView.findViewById(R.id.control_linearlayout));

        pauseStartLayout.setOnClickListener(this);
        vedioFast.setOnClickListener(this);
        vedioSlow.setOnClickListener(this);
        toEnd.setOnClickListener(this);
        toStart.setOnClickListener(this);

        slowrateTextView.setVisibility(View.INVISIBLE);
        fastrateTextView.setVisibility(View.INVISIBLE);

        this.vedioProcess = VedioProcess.initVedioProcess(context);
        surfaceViewList = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.temCalendar = Calendar.getInstance();

        mpSystemTime = new Player.MPSystemTime();
    }

    public void showPlayControlView() {
        int viewWidth = CommonMethod.contentViewWidth(context) - (int) context.getResources().getDimension(R.dimen.test);
        viewWithd = viewWidth - leftMargin - rigthMargin;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                viewWithd,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        params.leftMargin = leftMargin;
        context.addContentView(playControlView, params);
    }

    public void invalidateView() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playControlView.getLayoutParams();
        if (params != null) {
            params.width = CommonMethod.contentViewWidth(context) - (int) context.getResources().getDimension(R.dimen.test);
            playControlView.setLayoutParams(params);
        }

    }

    public void removePlayControlView() {
        ViewGroup parentViewGroup = (ViewGroup) playControlView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeView(playControlView);
        }
    }

    /**
     * 设置 需要操作的 surfaceview
     *
     * @param surfaceView
     */
    public void setSurfaceView(PlaySurfaceView surfaceView) {
        currentPlaySurfaceView = surfaceView;
        if (!surfaceViewList.contains(surfaceView)) {
            surfaceViewList.add(surfaceView);
        }
    }

    /**
     * 传递需要的数据
     *
     * @param alarmHistoryBean
     */
    public void setAlarmHistoryBean(AlarmHistory alarmHistoryBean) {
        // miaoyc
        fillNetTime(originalNet_dvr_time, alarmHistoryBean.startTime);
        fillNetTime(startNet_dvr_time, alarmHistoryBean.currentTime);
        fillNetTime(endNet_dvr_time, alarmHistoryBean.endTime);
        setBufTime(alarmHistoryBean.endTime);
        playChannel = alarmHistoryBean.channel;
        calculationTimeDisparity(alarmHistoryBean.startTime, alarmHistoryBean.endTime);

        showSeekBarBg(alarmHistoryBean);
    }

    public void setBufTime(int[] time) {
        int[] calendarTime = new int[6];
        Calendar calendar = Calendar.getInstance();
        calendar.set(time[0], time[1], time[2], time[3], time[4], time[5]);
        calendar.add(Calendar.SECOND, -5);
        calendarTime[0] = calendar.get(Calendar.YEAR);
        calendarTime[1] = calendar.get(Calendar.MONTH);
        calendarTime[2] = calendar.get(Calendar.DAY_OF_MONTH);
        calendarTime[3] = calendar.get(Calendar.HOUR_OF_DAY);
        calendarTime[4] = calendar.get(Calendar.MINUTE);
        calendarTime[5] = calendar.get(Calendar.SECOND);

        fillNetTime(buf_dvr_time, calendarTime);
    }

    private void showSeekBarBg(AlarmHistory alarmHistoryBean) {
        if (totalDisparity > 0) {
            PlayProgressBean playProgressBeanF = new PlayProgressBean();
            playProgressBeanF.interval = totalDisparity - 6 * 1000;
            playProgressBeanF.type = CommonAttribute.Play_seekbar_alarmType;

            alarmHistoryBean.playProgressBeans.set(1, playProgressBeanF);
            showSeekerProgressBg(alarmHistoryBean.playProgressBeans);
        }
    }

    /**
     * 该方法调用时需要先调用
     * setSurfaceView(PlaySurfaceView surfaceView)
     * setAlarmHistoryBean(AlarmHistory alarmHistoryBean)
     */
    public void startPlayback() {
        startPlayBack(playChannel, startNet_dvr_time, endNet_dvr_time);
    }

    /**
     * 停止回放
     */
    public void stopPlayBack() {
        for (PlaySurfaceView surfaceView : surfaceViewList) {
            surfaceView.setBackgroundColor(context.getResources().getColor(R.color.gray));
            vedioProcess.stopPlayBack(surfaceView);
        }
        stopTimerGetPlayProgress();
        setStartPauseBg(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vedio_play_start_pause_layout:
                pauseAndPlay();
                break;
            case R.id.vedio_play_fast_bt:
                surfaceViewFast();
                playRate++;
                showPlayRate();
                break;
            case R.id.vedio_play_slow_bt:
                surfaceViewSlow();
                playRate--;
                showPlayRate();
                break;
            case R.id.vedio_play_to_end:
                stopPlayBack();
                //预留几秒的播放时间
                startPlayBack(playChannel, buf_dvr_time, endNet_dvr_time);
                break;
            case R.id.vedio_play_to_start:
                stopPlayBack();
                startPlayBack(playChannel, originalNet_dvr_time, endNet_dvr_time);
                break;
        }
    }

    /**
     * 显示快进的倍数
     */
    private void showPlayRate() {
        NumberFormat nf = new DecimalFormat("#");

        vedioFast.setClickable(true);
        vedioSlow.setClickable(true);

        if (playRate > 0 && playRate <= 4) {
            fastrateTextView.setText("x" + nf.format(Math.pow(2, playRate)));
            fastrateTextView.setVisibility(View.VISIBLE);
            slowrateTextView.setVisibility(View.INVISIBLE);
            if (playRate == 4)
                vedioFast.setClickable(false);
        } else if (playRate < 0 && playRate >= -4) {
            slowrateTextView.setText("1/" + nf.format(Math.pow(2, -playRate)));
            fastrateTextView.setVisibility(View.INVISIBLE);
            slowrateTextView.setVisibility(View.VISIBLE);
            if (playRate == -4)
                vedioSlow.setClickable(false);
        } else if (playRate == 0) {
            fastrateTextView.setVisibility(View.INVISIBLE);
            slowrateTextView.setVisibility(View.INVISIBLE);
        }

    }

    private void calculationTimeDisparity(int[] sTime, int[] endTime) {
        startCalendar = Calendar.getInstance();
        startCalendar.set(sTime[0], sTime[1], sTime[2], sTime[3], sTime[4], sTime[5]);

        endCalendar = Calendar.getInstance();
        endCalendar.set(endTime[0], endTime[1], endTime[2], endTime[3], endTime[4], endTime[5]);
        totalDisparity = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        LogFactory.i(TAG, "totalDisparity===" + totalDisparity);
        updateTotalTime(totalDisparity);
    }

    /**
     * 设置seekbar 背景
     *
     * @param
     */
    private void showSeekerProgressBg(List<PlayProgressBean> intervalList) {
        int size = intervalList.size();

        progressLayout.removeAllViews();
        for (int i = 0; i < size; i++) {
            PlayProgressBean playProgressBean = intervalList.get(i);
            ImageView imageView = new ImageView(context);
            int ivWidth = (int) ((playProgressBean.interval * 1.0 / totalDisparity) * (viewWithd - 5));

            LogFactory.i(TAG, "ivWidth====" + ivWidth + "===viewWithd===" + viewWithd);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ivWidth, 10);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundColor(context.getResources().getColor(playSeekbarShowbg(playProgressBean.type)));
            progressLayout.addView(imageView);

            if (i < size - 1) {
                ImageView division = new ImageView(context);
                division.setBackgroundResource(R.drawable.play_seek_division);
                ViewGroup.LayoutParams divisionlayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 10);
                division.setLayoutParams(divisionlayoutParams);
                progressLayout.addView(division);
            }
        }
    }

    private int playSeekbarShowbg(int type) {
        int colorId = R.color.white;
        switch (type) {
            case CommonAttribute.Play_seekbar_start_endType:
                colorId = R.color.play_seekbar_start_end;
                break;
            case CommonAttribute.Play_seekbar_alarmType:
                colorId = R.color.play_seekbar_alarm;
                break;
        }
        return colorId;
    }


    private void startTimerGetPlayProgeress() {
        handler.postDelayed(handlerRunnable, delayTime);
    }

    private void stopTimerGetPlayProgress() {
        preDisTmer = 0;
        isStopRun = false;
        handler.removeCallbacks(handlerRunnable);

    }

    Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isStopRun) {
                LogFactory.e(TAG, "isStopRun" + isStopRun);
                mpSystemTime = new Player.MPSystemTime();
                currentPlaySurfaceView.getSystemTime(mpSystemTime);
                temCalendar = Calendar.getInstance();
                temCalendar.set(mpSystemTime.year, mpSystemTime.month, mpSystemTime.day, mpSystemTime.hour, mpSystemTime.min, mpSystemTime.sec);
                long disTime = temCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
                LogFactory.e(TAG, "disTime" + disTime);
                if (disTime < 0) {
                    if (disNumber == 100) {
                        disNumber = 0;
                        isStopRun = false;
                        return;
                    } else {
                        disNumber++;
                    }
                } else {
                    disNumber = 0;
                    updateProgressCurrent(disTime);
                }

                startTimerGetPlayProgeress();
            }
        }
    };


    private void updateProgressCurrent(long currentTime) {

        LogFactory.i(TAG, "currentTime====" + currentTime + "==preDisTmer==" + preDisTmer);

        if (currentTime > preDisTmer) {
            updateCurrentTime(currentTime);
            int cprogress = (int) (currentTime * 1.0 / totalDisparity * fraction);
            LogFactory.e(TAG, "cprogress====" + cprogress + "max====" + progressSeekbar.getMax());
            progressSeekbar.setProgress(cprogress);
            preDisTmer = currentTime;
        } else {
            if (totalDisparity <= preDisTmer) {
                //停止
                stopPlayBack();
            }
        }

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //设置 currentTime
        LogFactory.i(TAG, "progress====" + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public Calendar getCurrentTime() {
        int progress = progressSeekbar.getProgress();
        long currentTimer = (long) timerProportion * progress;

        temCalendar = Calendar.getInstance();
        temCalendar.setTimeInMillis(startCalendar.getTimeInMillis() + currentTimer);

        return temCalendar;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //进行 重新定位 播放
        int progress = seekBar.getProgress();
        long currentTimer = (long) timerProportion * progress;

        temCalendar = Calendar.getInstance();
        temCalendar.setTimeInMillis(startCalendar.getTimeInMillis() + currentTimer);
        current_dvr_time = new NET_DVR_TIME();
        current_dvr_time.dwYear = temCalendar.get(Calendar.YEAR);
        current_dvr_time.dwMonth = temCalendar.get(Calendar.MONTH);
        current_dvr_time.dwDay = temCalendar.get(Calendar.DATE);
        current_dvr_time.dwHour = temCalendar.get(Calendar.HOUR_OF_DAY);
        current_dvr_time.dwMinute = temCalendar.get(Calendar.MINUTE);
        current_dvr_time.dwSecond = temCalendar.get(Calendar.SECOND);
        stopPlayBack();
        startPlayBack(playChannel, current_dvr_time, endNet_dvr_time);
    }

    private void fillNetTime(NET_DVR_TIME net_dvr_time, int[] sTime) {
        net_dvr_time.dwYear = sTime[0];
        net_dvr_time.dwMonth = sTime[1];
        net_dvr_time.dwDay = sTime[2];
        net_dvr_time.dwHour = sTime[3];
        net_dvr_time.dwMinute = sTime[4];
        net_dvr_time.dwSecond = sTime[5];

    }


    public void setBackgroundColor(int color) {
        controlLayout.setBackgroundColor(context.getResources().getColor(color));
    }

    public void setShowTimeView(int visibility) {
        showTimeView.setVisibility(visibility);
    }

    public void setSeekbarThumb(int drawableId) {
        progressSeekbar.setThumb(context.getResources().getDrawable(drawableId));
    }

    /**
     * 设置 left margin
     *
     * @param leftMargin
     */
    public void setControlViewLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    /**
     * 设置 right margin
     *
     * @param rightMargin
     */
    public void setControlViewRightMargin(int rightMargin) {
        this.rigthMargin = rightMargin;
    }

    private void surfaceViewFast() {
        for (PlaySurfaceView playSurfaceView : surfaceViewList) {
            playSurfaceView.fast();
        }
    }

    private void surfaceViewSlow() {
        for (PlaySurfaceView playSurfaceView : surfaceViewList) {
            playSurfaceView.slow();
        }
    }

    private int getNumber(PlaySurfaceView surfaceView) {
        for (int i = 0; i < surfaceViewList.size(); i++) {
            if (surfaceView.equals(surfaceViewList.get(i)))
                return i;
        }

        return -1;
    }

    private void startPlayBack(int channel, NET_DVR_TIME startTime, NET_DVR_TIME endTime) {
        for (PlaySurfaceView surfaceView : surfaceViewList) {
            surfaceView.setBackgroundColor(context.getResources().getColor(R.color.gray));
            vedioProcess.startPlayBack(surfaceView, channel, startTime, endTime);

//            EventBus.getDefault().post(new EventBusBean(getNumber(surfaceView)));
        }
        isStopRun = true;
        startTimerGetPlayProgeress();
        setStartPauseBg(true);
    }

    /**
     * 暂停，恢复
     */
    private void pauseAndPlay() {
        if (isPlay) {
            surfaceViewPause(playPause);
            startPause.setBackgroundResource(R.drawable.play_restart);
            stopTimerGetPlayProgress();
            // 获取图片，显示，且用imageview显示出来
            // 判断是不是全屏播放的时候
            if (isFullScreen())
                currentPlaySurfaceView.getImageDate();

        } else {
            surfaceViewPause(playRecovery);
            startPause.setBackgroundResource(R.drawable.play_pause);
            isStopRun = true;
            startTimerGetPlayProgeress();
            if (isFullScreen())
                EventBus.getDefault().post(new EventBusBean("invisible"));
        }
        isPlay = !isPlay;
    }

    private void surfaceViewPause(int pauseTag) {
        for (PlaySurfaceView playSurfaceView : surfaceViewList) {
            playSurfaceView.pause(pauseTag);
        }
    }

    private void showPauseBg() {
        if (isPlay) {
            startPause.setBackgroundResource(R.drawable.play_pause);
        } else {
            startPause.setBackgroundResource(R.drawable.play_restart);
        }
    }

    /**
     * 更新总时间
     *
     * @param totalTimeNumber 单位是毫秒
     */
    private void updateTotalTime(long totalTimeNumber) {
//        String stime = simpleDateFormat.format(new Date(totalTimeNumber));
        String stime = CommonMethod.FormatMis(totalTimeNumber / 1000);
        ((TextView) totalTimeTV).setText(stime);
        progressSeekbar.setMax(fraction);
        timerProportion = totalTimeNumber * 1.0 / fraction;
        LogFactory.i(TAG, "timerProportion===" + timerProportion);
    }

    /**
     * 更新当前时间
     *
     * @param currentTimeNumber
     */
    private void updateCurrentTime(long currentTimeNumber) {
//        String stime = simpleDateFormat.format(new Date(currentTimeNumber));
        String stime = CommonMethod.FormatMis(currentTimeNumber / 1000);
        ((TextView) currentTime).setText(stime);
    }

    /**
     * 设置 显示的播放状态
     *
     * @param playTag true is play false is pause
     */
    private void setStartPauseBg(boolean playTag) {
        this.isPlay = playTag;
        showPauseBg();
    }

    public void onDestory() {
        //    stopPlayBack();
        surfaceViewList.clear();
    }

    public void setViewUnclick(boolean enable) {
        pauseAndPlay();
        disableEnableControls(enable, (ViewGroup) playControlView);
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);

            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }

    // 判断是否是历史回放全屏界面
    public boolean isFullScreen() {
        return VedioAlarmHistoryFullPlayActivity.class.getName().equals(context.getComponentName().getClassName());
    }
}
