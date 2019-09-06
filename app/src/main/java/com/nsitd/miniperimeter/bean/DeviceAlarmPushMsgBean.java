package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/4/18.
 */
public class DeviceAlarmPushMsgBean {
    public String method;
    public String message;
    public String state;
    public DeviceAlarmPushMsg content;

    public static class DeviceAlarmPushMsg {
        public String type;
        public String devsn;
        /*0:无告警 1:有告警 */
        public String isalarm;
        /**
         * 1:通信异常
         2:低压放电告警
         3:高压放电告警
         4:低压充电告警
         5:高压充电告警
         6:电流过载告警
         7:低温告警
         8:高温告警
         9:读卡线缆版本不一致
         a:欠压告警
         b:向量丢失告警
         c:短路告警(cc)
         */
        public String alarmtype;
        public String time;
    }
}
