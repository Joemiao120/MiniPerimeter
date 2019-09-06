package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class LineLengthBean extends AbsBaseBean<LineLengthBean.LineLength>{

    public static class LineLength{
        public String aLineId;
        public String aLineLength;
        public String bLineId;
        public String bLineLength;
    }
}
