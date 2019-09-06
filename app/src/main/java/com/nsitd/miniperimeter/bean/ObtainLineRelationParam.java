package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/22.
 */
public class ObtainLineRelationParam extends AbsBaseParam<ObtainLineRelationParam.ObtainLineRelationContent>{

    public ObtainLineRelationParam(){
        content = new ObtainLineRelationContent();
    }
    public static class ObtainLineRelationContent{
        public String resourceId;
        public String resourceType;
    }
}
