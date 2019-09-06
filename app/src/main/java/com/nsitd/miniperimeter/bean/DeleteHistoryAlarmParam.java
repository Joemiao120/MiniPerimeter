package com.nsitd.miniperimeter.bean;

/**
 * 17,删除历史告警
 * Created by reimu on 16/3/24.
 */
public class DeleteHistoryAlarmParam extends AbsBaseParam<DeleteHistoryAlarmParam.DeleteHistroyAlarmContent> {
    public DeleteHistoryAlarmParam() {
        content = new DeleteHistroyAlarmContent();
    }

    public static class DeleteHistroyAlarmContent {
        public String alarmIds;
    }
}
