package com.nsitd.miniperimeter.video.custom_view;

import android.app.Activity;
import android.app.Service;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.nsitd.miniperimeter.R;
import com.nsitd.miniperimeter.util.CommonAttribute;
import com.nsitd.miniperimeter.util.LogFactory;

/**
 * Created by happy on 16/3/25.
 */
public class AdjustmentVoiceView implements SeekBar.OnSeekBarChangeListener {

    private Activity context;
    private View adjustmentVoiceView;
    private FrameLayout.LayoutParams layoutParams;
    private AudioManager audioManager = null;
    private int current;
    private SeekBar voiceSeekbar;
    private int mMaxVolume;

    public AdjustmentVoiceView(Activity context) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        adjustmentVoiceView = inflater.inflate(R.layout.vedio_adjustment_voice, null);

        voiceSeekbar = ((SeekBar) adjustmentVoiceView.findViewById(R.id.adjustment_voice_seekbar));
        voiceSeekbar.setOnSeekBarChangeListener(this);
        layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //获取当前值
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        voiceSeekbar.setMax(mMaxVolume);
        voiceSeekbar.setProgress(current);

    }


    public void showAdjustmentVoice() {

        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = CommonAttribute.ShowContentViewWidth - (int) (context.getResources().getDimension(R.dimen.activity_vedio_adjustment_bright_leftmargin) * 2.5);
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
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 1);
//        current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); //获取当前值
//        if (progress < current) {
//
//           audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//        } else {
//            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//        }
    }

    public void setVoiceSeekbar(float percent, float distanceY) {
        float volumeOffsetAccurate = mMaxVolume * percent * 3;
        int volumeOffset = (int) volumeOffsetAccurate;

        if (volumeOffset == 0 && Math.abs(volumeOffsetAccurate) > 0.2f) {
            if (distanceY > 0) {
                volumeOffset = 1;
            } else if (distanceY < 0) {
                volumeOffset = -1;
            }
        }

        current += volumeOffset;
        if (current > mMaxVolume)
            current = mMaxVolume;
        else if (current < 0)
            current = 0;

        voiceSeekbar.setProgress(current);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
