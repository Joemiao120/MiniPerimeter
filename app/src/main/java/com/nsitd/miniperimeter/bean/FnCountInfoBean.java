package com.nsitd.miniperimeter.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reimu on 16/4/20.
 */
public class FnCountInfoBean extends AbsBaseBean<FnCountInfoBean.FnCountInfoList>{

    public static class FnCountInfoList{
        public List<FnCountInfo> fncountlists = new ArrayList<>();
    }

    public static class FnCountInfo implements Serializable{
        public String port;
        public String fnbasecount;
        public String fnruncount;
        public String fnerrorcount;
        public List<FnInfo> fnerrorInfos = new ArrayList<>();
    }

    public static class FnInfo{
        public String fnsn;
        public String index;
        public String errormsg;
    }
}
