package com.nsitd.miniperimeter.video.bean;

import com.nsitd.miniperimeter.video.custom_view.PlaySurfaceView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by happy on 16/3/25.
 */
public class AlarmHistory implements Serializable{

    public int channel;
    public int[] startTime;
    public int [] endTime;
    public int [] currentTime ;
    public String channelName;
    public String channelId;

    public List<PlayProgressBean> playProgressBeans;
    public int alarmTag = 0;//1为告警，0为正常


}
