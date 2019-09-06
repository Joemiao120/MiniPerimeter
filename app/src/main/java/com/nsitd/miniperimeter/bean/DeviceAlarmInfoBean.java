package com.nsitd.miniperimeter.bean;

import java.util.List;

/**
 * Created by reimu on 16/3/25.
 */
public class DeviceAlarmInfoBean extends AbsBaseBean<DeviceAlarmInfoBean.DeviceAlarmInfo>{

    public static class DeviceAlarmInfo{
        public List<FnInfo> fnList;
    }

    public static class FnInfo{
        public List<StandardInfo> standardList;
    }

    public static class StandardInfo{
        public String index;
        public String fnsn;
        /*0:正常1:缺失|2:替换|3:变序*/
        public String state;
    }

}
