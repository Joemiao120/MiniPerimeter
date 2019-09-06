package com.nsitd.miniperimeter.bean;

import java.util.List;

/**
 * Created by reimu on 16/3/22.
 */
public class ObtainLineRelationBean extends AbsBaseBean<ObtainLineRelationBean.LineRelationContent> {

    public static class LineRelationContent {
        public String resourceId;
        public String resourceName;
        public List<LineRelation> lineRelationList;
    }

    public static class LineRelation {
        public String lineRelationId;
        public String alineId;
        public String alineStartNum;
        public String alineEndNum;
        public String blineId;
        public String blineStartNum;
        public String blineEndNum;
    }
}
