package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/22.
 */
public class HistoryAlarmParam extends AbsBaseParam<HistoryAlarmParam.HistroyAlarmContent> {

    public HistoryAlarmParam(){
        content = new HistroyAlarmContent();
    }

    public static class HistroyAlarmContent{
        public String timeType;
        public String pageNo;
        public String pageSize;
    }
}
