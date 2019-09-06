package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class SetSensitivityParam extends AbsBaseParam<SetSensitivityParam.SetSensitivityContent>{

    public SetSensitivityParam(){
        content = new SetSensitivityContent();
    }

    public static class SetSensitivityContent{
        public String sensitivityId;
        public String sensitivityType;
    }
}
