package com.nsitd.miniperimeter.activity.vedio;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.video.VedioProcess;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;
import com.nsitd.miniperimeter.video.custom_view.VedioPlayControlView;
import com.nsitd.miniperimeter.video.inter.IVedioCallback;

import org.MediaPlayer.PlayM4.Player;

import java.util.List;

/**
 * Created by happy on 16/3/18.
 */
public class VedioGridViewAdapter extends BaseAdapter {

    private String TAG = VedioGridViewAdapter.class.getSimpleName();
    private final static int LOGVIEW_CHANGE = 0;

    private LayoutInflater inflater;
    private DisplayMetrics metric;
    private Context context;
    private int margin = 0;
    private VedioProcess vedioProcess = null;
    private int viewWith;

    private double setProportion = 0;
    private List<AlarmHistory> cameraList = null;
    private PlaySurfaceView[] playSurfaceViews = null;
    private View[] playSurfaceLayout = null, tem, waitArrayView;
    private TextView[] logArrayView;
    private ImageView[] cameraView = null;
    private AlarmHistory alarmHistory = null;
    private int listSize = 0;
    private int totalNumber;
    private int flag;

    private Handler handler = new Handler();
    /**
     * 回放 设置
     */
    private VedioPlayControlView vedioPlayControlView;

    public VedioGridViewAdapter(Context context, int numColumns, int totalNumber, int flag) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.totalNumber = totalNumber;
        this.flag = flag;

        metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);

        int layoutpadding = (int) context.getResources().getDimension(R.dimen.activity_vedio_gridview_item_padding);
        int titleBar = (int) context.getResources().getDimension(R.dimen.activity_vedio_title_bar);

        vedioProcess = VedioProcess.initVedioProcess(context);
        playSurfaceViews = new PlaySurfaceView[totalNumber];
        playSurfaceLayout = new View[totalNumber];
        tem = new View[totalNumber];
        cameraView = new ImageView[totalNumber];
        waitArrayView = new ProgressBar[totalNumber];
        logArrayView = new TextView[totalNumber];

        metricLayout(numColumns);

    }

    /**
     * 设置 内容
     *
     * @param cameraList
     */
    public void setCameraList(List<AlarmHistory> cameraList) {
        this.cameraList = cameraList;
        listSize = cameraList.size();
    }

    /**
     * 设置 列数
     *
     * @param numColumns
     */
    public void metricLayout(int numColumns) {
        int layouthorizontalSpacing = (int) context.getResources().getDimension(R.dimen.activity_vedio_gridview_verticalSpacing);
        margin = layouthorizontalSpacing * 4;
        int statusBarHeight = CommonMethod.getStatusBarHeight(context);
        int viewHight = metric.heightPixels - statusBarHeight;
        viewWith = (metric.widthPixels - margin) / numColumns;
        setProportion = 1.0 * viewHight / metric.widthPixels;

//        viewHight = CommonMethod.contentViewHight(context) - margin;
//        viewWith = (CommonMethod.contentViewWidth(context) - margin) / numColumns;
//        setProportion = 1.0 * viewHight / CommonMethod.contentViewWidth(context);
    }

    @Override
    public int getCount() {

//        if (cameraList != null)
//            return cameraList.size();
        return totalNumber;
    }

    @Override
    public Object getItem(int position) {

        return cameraList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (parent.getChildCount() == position) {
            if (convertView == null) {

                viewHolder = new ViewHolder();
                convertView = this.inflater.inflate(R.layout.vedio_gridview_item, null);
                viewHolder.layout = convertView.findViewById(R.id.vedio_layout);
                viewHolder.surfaceLy = ((ViewGroup) convertView.findViewById(R.id.vedio_surface_layout));
                viewHolder.textView = ((TextView) convertView.findViewById(R.id.title_textview));
                viewHolder.surfaceView = ((SurfaceView) convertView.findViewById(R.id.surface_previous));
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.no_camera);
                viewHolder.waitingView = (ProgressBar) convertView.findViewById(R.id.wait_progress);

                PlaySurfaceView playView = new PlaySurfaceView(context);
                playView.setProportion(setProportion);
                playView.setParam(viewWith);
//                playView.setSizeCallback(new PlaySurfaceView.SetSizeCallback() {
//                    @Override
//                    public int getWidth() {
//                        metricLayout(3);
//                        return viewWith;
//                    }
//
//                    @Override
//                    public int getHeight() {
//                        metricLayout(3);
//                        return (int) (viewWith * setProportion);
//                    }
//                });
                playSurfaceViews[position] = playView;
                viewHolder.surfaceLy.addView(playView, 0);

                viewHolder.surfaceLy.setBackgroundColor(context.getResources().getColor(R.color.gray));
                viewHolder.surfaceView.setBackgroundColor(context.getResources().getColor(R.color.gray));

                convertView.setTag(viewHolder);
                playSurfaceLayout[position] = viewHolder.surfaceLy;
                tem[position] = viewHolder.surfaceView;
                cameraView[position] = viewHolder.imageView;
                waitArrayView[position] = viewHolder.waitingView;
                logArrayView[position] = viewHolder.textView;

                Log.i(TAG, "convertView====" + position + "==playView==" + playView);

            } else {
                viewHolder = ((ViewHolder) convertView.getTag());
            }
        } else {
            //由于child view 重新计算宽高，会出现多次调用，这个分支显示的是临时对象
            return parent.getChildAt(position);
        }
        Log.i(TAG, "convertView====111" + position);
        if (position < listSize) {
            alarmHistory = cameraList.get(position);
//            viewHolder.surfaceView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            if (alarmHistory != null) {
                if (alarmHistory.alarmTag == CommonAttribute.VedioAlarm) {
                    viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.home_vedio_alarm_bg));
                    viewHolder.textView.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
//                    viewHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
                }
            }
            if (flag == 1 && !("".equals(alarmHistory.channelName))) {
                viewHolder.textView.setText(alarmHistory.channelName);
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

        return convertView;
    }

    /**
     * 预览，停止预览
     *
     * @param isPreview true is startpreview false is stoppreview
     */
    public void startPreview(boolean isPreview) {
        for (int i = 0; i < listSize; i++) {
            if (isPreview) {

                showWaitingView(playSurfaceViews[i], i);

                boolean result = vedioProcess.startVedioPreview(playSurfaceViews[i], cameraList.get(i).channel, false);
                if (!result) {
                    playSurfaceLayout[i].setBackgroundColor(context.getResources().getColor(R.color.gray));
                    tem[i].setBackgroundColor(context.getResources().getColor(R.color.gray));
                    playSurfaceViews[i].setBackgroundColor(context.getResources().getColor(R.color.gray));
                    setLogText(i, "登陆摄像头失败");
                } else {
                    setLogText(i, "登陆摄像头成功");
                    final int number = i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setLogText(number, cameraList.get(number).channelName);
                        }
                    }, 1000);
                }
            } else {
                playSurfaceLayout[i].setBackgroundColor(context.getResources().getColor(R.color.gray));
                tem[i].setBackgroundColor(context.getResources().getColor(R.color.gray));

                vedioProcess.stopVedioPreview(playSurfaceViews[i]);
            }
        }

    }


    public void setVedioPlayControlView(VedioPlayControlView vedioPlayControlView) {
        this.vedioPlayControlView = vedioPlayControlView;
    }

    public void setLogText(int number, String showString) {
        logArrayView[number].setText(showString);
        logArrayView[number].setTextColor(context.getResources().getColor(R.color.black));
    }

    /**
     * 设置回放需要的参数
     *
     * @param alarmHistory
     */
    public void setPlaybackSurface(AlarmHistory alarmHistory) {
        for (int position = 0; position < listSize; position++) {
//            playSurfaceLayout[position].setBackgroundColor(context.getResources().getColor(R.color.transparent));
//            tem[position].setBackgroundColor(context.getResources().getColor(R.color.transparent));
            showWaitingView(playSurfaceViews[position], position);
            vedioPlayControlView.setSurfaceView(playSurfaceViews[position]);
        }
        vedioPlayControlView.setAlarmHistoryBean(alarmHistory);
    }

    /**
     * 设置正在加载界面
     */
    public void setWaitingView(boolean flag, int position) {
        if (flag) {
            waitArrayView[position].setVisibility(View.VISIBLE);
        } else {
            waitArrayView[position].setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 是否播放
     *
     * @param isPreview
     */

    public void startPlayback(boolean isPreview) {
        if (isPreview) {
            vedioPlayControlView.startPlayback();
        } else {
            vedioPlayControlView.stopPlayBack();
        }
    }

    /**
     * 显示等待界面
     *
     * @param surfaceView
     */
    private void showWaitingView(final PlaySurfaceView surfaceView, final int position) {
        surfaceView.setCallBack(new IVedioCallback() {
            @Override
            public void vedioProcessResultCallback(int result) {
                if (result == CommonAttribute.SUCCESS) {
                    int denry = 0;

                    if (CommonAttribute.VedioHomeLoadNumber == 0) {
                        //第一次为了规避透明看到其他组件
                        denry = 1000;
                    }
                    CommonAttribute.VedioHomeLoadNumber++;
                    Log.i(TAG, "加载数据结果----" + result + "==denry==" + denry);

                    if (result == CommonAttribute.SUCCESS) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playSurfaceLayout[position].setBackgroundColor(context.getResources().getColor(R.color.transparent));
                                tem[position].setBackgroundColor(context.getResources().getColor(R.color.transparent));
                                cameraView[position].setVisibility(View.INVISIBLE);
                            }
                        }, denry);
                    }
                } else {
                    playSurfaceLayout[position].setClickable(false);
                    tem[position].setClickable(false);
                }
            }
        });
    }

    public PlaySurfaceView getPlaySurfaceView(int postion) {

        try {
            return playSurfaceViews[postion];

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 得到当前播放时间
     *
     * @param postion
     * @param mpSystemTime
     */
    public boolean getSystemTime(int postion, Player.MPSystemTime mpSystemTime) {
        PlaySurfaceView playSurfaceView = getPlaySurfaceView(postion);
        return playSurfaceView.getSystemTime(mpSystemTime);
    }

    /**
     * 更新 告警背景
     */
    public void updateAlarmColor() {
        notifyDataSetChanged();
    }


    public static class ViewHolder {

        View layout;
        ViewGroup surfaceLy;
        TextView textView;
        SurfaceView surfaceView;
        ImageView imageView;
        ProgressBar waitingView;
    }

}
