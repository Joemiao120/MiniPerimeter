package com.nsitd.miniperimeter.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/3/22.
 */
public class ViewInitBean extends AbsBaseBean<ViewInitBean.ViewInit>{


    public static class ManagerResource{
        public String managerResourceId;
        public String managerResourceName;
        public String managerResourceIp;
        public String managerResourceLoginName;
        public String managerResourceLoginPwd;
        public String managerResourceLoginPort;
        public List<ViewResource> resourceList = new ArrayList<>();
    }


    public static class ViewInit{
       public List<ManagerResource> managerResourceList;

    }

    public static class ViewResource{
        public String resourceId;
        public String resourceName;
        public String managerResourceChannel;
    }
}
