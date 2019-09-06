package com.nsitd.miniperimeter.video.custom_view;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;

/**
 * Created by happy on 16/3/25.
 */
public class AdjustmentBrightView implements SeekBar.OnSeekBarChangeListener {

    private final String TAG = this.getClass().getSimpleName();
    private Activity context;
    private View adjustmentVoiceView;
    private FrameLayout.LayoutParams layoutParams;
    private SeekBar voiceSeekbar;
    private int screenBrightness;
    private int mMaxBright = 255;

    public AdjustmentBrightView(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        adjustmentVoiceView = inflater.inflate(R.layout.vedio_adjustment_voice, null);
        ImageView imageView = ((ImageView) adjustmentVoiceView.findViewById(R.id.play_adjustmet_iv));
        imageView.setImageResource(R.drawable.player_settings_bright);

        voiceSeekbar = ((SeekBar) adjustmentVoiceView.findViewById(R.id.adjustment_voice_seekbar));
        voiceSeekbar.setOnSeekBarChangeListener(this);
        layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        try {

            // 获得当前屏幕亮度值 0--255
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            LogFactory.i(TAG, "screenBrightness = " + screenBrightness);
            voiceSeekbar.setProgress(screenBrightness);
            voiceSeekbar.setMax(mMaxBright);

        } catch (Exception e) {

        }


    }


    public void showAdjustmentBright() {

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.activity_vedio_adjustment_bright_leftmargin);
        context.addContentView(adjustmentVoiceView, layoutParams);
    }

    public void removeAdjustmentVoice() {
        ViewGroup parentViewGroup = ((ViewGroup) adjustmentVoiceView.getParent());
        if (parentViewGroup != null) {
            parentViewGroup.removeView(adjustmentVoiceView);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        WindowManager.LayoutParams win = context.getWindow().getAttributes();
//        win.screenBrightness=(float)progress/255;
//        context.getWindow().setAttributes(win);
        setScreenBrightness(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    private void setScreenBrightness(float value) {
//        Window mWindow = context.getWindow();
//        WindowManager.LayoutParams mParams = mWindow.getAttributes();
//        float f = value / 255.0F;
//        mParams.screenBrightness = f;
//        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }

    public void setBrightSeekbar(float percent, float distanceY) {
        float volumeOffsetAccurate = mMaxBright * percent * 5;
        int volumeOffset = (int) volumeOffsetAccurate;

        if (volumeOffset == 0 && Math.abs(volumeOffsetAccurate) > 0.2f) {
            if (distanceY > 0) {
                volumeOffset = 1;
            } else if (distanceY < 0) {
                volumeOffset = -1;
            }
        }

        screenBrightness += volumeOffset;
        if (screenBrightness > mMaxBright)
            screenBrightness = mMaxBright;
        else if (screenBrightness < 0)
            screenBrightness = 0;

        voiceSeekbar.setProgress(screenBrightness);
    }


}
