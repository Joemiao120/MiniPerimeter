package com.nsitd.miniperimeter.activity.vedio.alarm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.hikvision.netsdk.HCNetSDK;
import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.activity.base.BaseActivity;
import com.nsitd.miniperimeter.bean.EventBusBean;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.CommonMethod;
import com.nsitd.miniperimeter.util.LogFactory;
import com.nsitd.miniperimeter.video.bean.AlarmHistory;
import com.nsitd.miniperimeter.video.custom_view.AdjustmentBrightView;
import com.nsitd.miniperimeter.video.custom_view.AdjustmentVoiceView;
import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;
import com.nsitd.miniperimeter.video.custom_view.VedioPlayControlView;
import com.nsitd.miniperimeter.video.custom_view.ZoomImageView;
import com.nsitd.miniperimeter.video.inter.IVedioCallback;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class VedioAlarmHistoryFullPlayActivity extends BaseActivity {
    private static final int VOICEVIEW = 0;
    private static final int BRIGHTVIEW = 1;

    public static final int CHANGE = 3;

    private final String TAG = this.getClass().getSimpleName();
    private PlaySurfaceView playSurfaceView;
    private AlarmHistory serializableExtra;
    private AdjustmentBrightView adjustmentBrightView;
    private AdjustmentVoiceView adjustmentVoiceView;
    private VedioPlayControlView vedioPlayControlView;
    private GestureDetector gestureDetector;

    private FrameLayout frameLayout;
    private ZoomImageView imageView;
    private int viewWidth;
    private int viewHight;

    //声明进度条对话框
    private ProgressDialog m_pDialog;
    private boolean isFrist = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VOICEVIEW:
                    removeVoiceView();
                    break;
                case BRIGHTVIEW:
                    removeBrightView();
                    break;
                case CHANGE:
                    vedioPlayControlView.invalidateView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vedio_full_play);
        super.onCreate(savedInstanceState);

        serializableExtra = ((AlarmHistory) getIntent().getSerializableExtra(CommonAttribute.VedioCameraMessage));

        this.vedioPlayControlView = new VedioPlayControlView(this);

        addPlaySurfaceview();
        vedioPlayControlView.setSeekbarThumb(R.drawable.play_progress_thumb);
        vedioPlayControlView.showPlayControlView();

        tv_tilte_bar.setText("历史回放");
        setRightMidView(serializableExtra.channelName);
        //play control
        vedioPlayControlView.setSurfaceView(playSurfaceView);
        vedioPlayControlView.setAlarmHistoryBean(serializableExtra);

        gestureDetector = new GestureDetector(VedioAlarmHistoryFullPlayActivity.this, onGestureListener);
        isFrist = true;
        playSurfaceView.setCallBack(new IVedioCallback() {
            @Override
            public void vedioProcessResultCallback(int result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        m_pDialog.dismiss();
                        playSurfaceView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }
                });
            }
        });
        EventBus.getDefault().register(this);

    }

    private void showDialog() {
        //创建ProgressDialog对象
        m_pDialog = new ProgressDialog(this);
        m_pDialog.setIndeterminate(true);
        m_pDialog.show();
        m_pDialog.setContentView(R.layout.dialog_process);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialog();

//        playSurfaceView.setBackgroundColor(getResources().getColor(R.color.gray));

        vedioPlayControlView.startPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        playSurfaceView.setBackgroundColor(getResources().getColor(R.color.gray));
        vedioPlayControlView.stopPlayBack();
        numberReceiver = 0;
    }

    private void addPlaySurfaceview() {
        viewHight = CommonMethod.contentViewHight(this);
        viewWidth = CommonMethod.contentViewWidth(this);
        LogFactory.i(TAG, "viewHight===" + viewHight);
        playSurfaceView = new PlaySurfaceView(this);
//        playSurfaceView = CommonAttribute.currentPlaySurfaceview;
        playSurfaceView.setScreenHight(viewHight);
        playSurfaceView.setScreenWidth(viewWidth);
//        playSurfaceView.setSizeCallback(new PlaySurfaceView.SetSizeCallback() {
//            @Override
//            public int getWidth() {
//                return CommonMethod.contentViewWidth(VedioAlarmHistoryFullPlayActivity.this) - (int) getResources().getDimension(R.dimen.test);
//            }
//
//            @Override
//            public int getHeight() {
//                return CommonMethod.contentViewHight(VedioAlarmHistoryFullPlayActivity.this);
//            }
//        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        addContentView(playSurfaceView, params);

        imageView = new ZoomImageView(this, null);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                viewWidth - (int) this.getResources().getDimension(R.dimen.test),
                viewHight);
        imageParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        addContentView(imageView, imageParams);
        imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void backClick() {
        Intent intent = new Intent();
        intent.putExtra("CurrentTime", vedioPlayControlView.getCurrentTime());
        setResult(RESULT_OK, intent);

        super.backClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("CurrentTime", vedioPlayControlView.getCurrentTime());
            setResult(RESULT_OK, intent);

            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        removeBrightView();
        removeVoiceView();

        vedioPlayControlView.removePlayControlView();
        vedioPlayControlView.onDestory();

        numberReceiver = 0;
        playSurfaceView = null;

        EventBus.getDefault().post(new EventBusBean("start"));
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    int width = playSurfaceView.getWidth();
                    int x = (int) e1.getX();
                    float percent = distanceY / (float) playSurfaceView.getMeasuredHeight();
                    Message message = new Message();

                    if (x > width || e1.getY() < playSurfaceView.getY()) {
                        return false;
                    }

                    if (x > width * 4.0 / 5) {
                        message.what = VOICEVIEW;
                        handler.removeMessages(message.what);

                        if (adjustmentVoiceView == null) {
                            adjustmentVoiceView = new AdjustmentVoiceView(VedioAlarmHistoryFullPlayActivity.this);
                            adjustmentVoiceView.showAdjustmentVoice();
                        }

                        // 开始调节音量
                        adjustmentVoiceView.setVoiceSeekbar(percent, distanceY);
                        handler.sendMessageDelayed(message, 2000);
                    } else if (x < width / 5.0) {
                        message.what = BRIGHTVIEW;
                        handler.removeMessages(message.what);

                        if (adjustmentBrightView == null) {
                            adjustmentBrightView = new AdjustmentBrightView(VedioAlarmHistoryFullPlayActivity.this);
                            adjustmentBrightView.showAdjustmentBright();
                        }
                        // 开始调节亮度
                        adjustmentBrightView.setBrightSeekbar(percent, distanceY);
                        handler.sendMessageDelayed(message, 2000);
                    }
                    return true;
                }

            };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void removeVoiceView() {
        if (adjustmentVoiceView != null) {
            adjustmentVoiceView.removeAdjustmentVoice();
            adjustmentVoiceView = null;
        }
    }

    public void removeBrightView() {
        if (adjustmentBrightView != null) {
            adjustmentBrightView.removeAdjustmentVoice();
            adjustmentBrightView = null;
        }
    }

    @Override
    public void handleReceive(Context context) {
        super.handleReceive(context);
        if (numberReceiver > 0) {
            vedioPlayControlView.setViewUnclick(isConnecting);
        }
        numberReceiver++;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUserEvent(EventBusBean event) {
        if ("visible".equals(event.getMsg())) {

            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/" + CommonAttribute.NAME_IMAGE);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // 设置想要的大小
            int newWidth = viewWidth - (int) this.getResources().getDimension(R.dimen.test);
            int newHeight = viewHight;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);

            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        } else if ("invisible".equals(event.getMsg())) {
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
