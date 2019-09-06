package com.nsitd.miniperimeter.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/22.
 */
public class HistoryAlarmBean extends AbsBaseBean<HistoryAlarmBean.HistroyAlarmList>{

    public static class HistroyAlarmList{
        public List<HistroyAlarm> alarmArray = new ArrayList<>();
        public String totalNumber;

    }

    public static class HistroyAlarm{
        public String alarmId;
        public String startTime;
        public String endTime;
        public String location;
    }

}
