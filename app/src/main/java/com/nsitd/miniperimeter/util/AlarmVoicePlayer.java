package com.nsitd.miniperimeter.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.nsitd.miniperimeter.R;

/**
 * Created by mac on 16/4/22.
 */
public class AlarmVoicePlayer {

    private static AlarmVoicePlayer INSTANCE = new AlarmVoicePlayer();
    private MediaPlayer mediaPlayer;
    // 程序的Context对象
    private Context mContext;

    private AlarmVoicePlayer() {
    }

    public static AlarmVoicePlayer getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0, 0);
    }

    public void play() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
