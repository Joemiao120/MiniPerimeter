package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/24.
 */
public class ObtainResourceParam extends AbsBaseParam<ObtainResourceParam.ObtainResourceContent>{

    public ObtainResourceParam(){
        content = new ObtainResourceContent();
    }

    public static class ObtainResourceContent{
        public String resourceId;
        public String resourceType;
    }
}
