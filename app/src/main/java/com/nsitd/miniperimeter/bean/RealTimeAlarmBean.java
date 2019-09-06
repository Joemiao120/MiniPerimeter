package com.nsitd.miniperimeter.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/22.
 */
public class RealTimeAlarmBean extends AbsBaseBean<RealTimeAlarmBean.RealTimeAlarmList>{

    public static class RealTimeAlarmList {
        public List<RealTimeAlarm> alarmArray = new ArrayList<>();
    }

    public static class RealTimeAlarm {
        public String videoId;
        public String fnSn;
        public String location;
    }
}
