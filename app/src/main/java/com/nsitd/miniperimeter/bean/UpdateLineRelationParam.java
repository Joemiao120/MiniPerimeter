package com.nsitd.miniperimeter.bean;

/**
 * 视频与线关联信息更新
 * Created by reimu on 16/3/24.
 */
public class UpdateLineRelationParam extends AbsBaseParam<UpdateLineRelationParam.UpdateLineRelationContent>{
    public UpdateLineRelationParam() {
        content = new UpdateLineRelationContent();
    }

    public static class UpdateLineRelationContent {
        public String resourceId;
        public String resourceType;
        public String alineId;
        public String alineStartNum;
        public String alineEndNum;
        public String blineId;
        public String blineStartNum;
        public String blineEndNum;
        public String lineRelationId;
        public String resourceName;
    }
}
