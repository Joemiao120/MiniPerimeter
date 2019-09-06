package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/4/14.
 */
public class DeleteSomedayHistoryAlarmParam extends AbsBaseParam<DeleteSomedayHistoryAlarmParam.DeleteSomedayHistroyAlarm> {

    public DeleteSomedayHistoryAlarmParam() {
        content = new DeleteSomedayHistroyAlarm();
    }
    public static class DeleteSomedayHistroyAlarm{
        public String timeType;
    }
}
