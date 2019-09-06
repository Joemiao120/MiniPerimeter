package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class SetLineLengthParam extends AbsBaseParam<SetLineLengthParam.SetLineLengthCotent>{

    public SetLineLengthParam(){
        content = new SetLineLengthCotent();
    }
    public static class SetLineLengthCotent{
        public String aLineId;
        public String aLineLength;
        public String bLineId;
        public String bLineLength;
    }
}
