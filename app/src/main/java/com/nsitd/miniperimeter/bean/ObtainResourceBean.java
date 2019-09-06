package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/24.
 */
public class ObtainResourceBean extends AbsBaseBean<ObtainResourceBean.ObtainResource>{

    public static class ObtainResource{
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
