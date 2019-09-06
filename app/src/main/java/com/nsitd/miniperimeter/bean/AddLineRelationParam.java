package com.nsitd.miniperimeter.bean;

/**
 * 视频与线关联信息保存
 * Created by reimu on 16/3/24.
 */
public class AddLineRelationParam extends AbsBaseParam<AddLineRelationParam.AddLineRelationContent> {

    public AddLineRelationParam() {
        content = new AddLineRelationContent();
    }

    public static class AddLineRelationContent {
        public String resourceId;
        public String resourceType;
        public String alineId;
        public String alineStartNum;
        public String alineEndNum;
        public String blineId;
        public String blineStartNum;
        public String blineEndNum;
        public String resourceName;
    }
}
