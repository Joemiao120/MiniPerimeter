package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/22.
 */
public class CameraInfoBean extends AbsBaseBean<CameraInfoBean.CameraInfo>{

    public static class CameraInfo{
        public String resourceId;
        public String resourceName;
        public String managerResourceId;
        public String managerResourceName;
        public String managerResourceIp;
        public String managerResourceLoginName;
        public String managerResourceLoginPwd;
        public String managerResourceLoginPort;
        public String managerResourceChannel;
    }
}
