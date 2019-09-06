package com.nsitd.miniperimeter.bean;

/**
 * 22、设置布防/撤防模式
 * Created by reimu on 16/3/24.
 */
public class SetDefenseModelParam extends AbsBaseParam<SetDefenseModelParam.SetDefenseModelContent>{

    public SetDefenseModelParam(){
        content = new SetDefenseModelContent();
    }
    public static class SetDefenseModelContent{
        /*1:布防，0：撤防*/
        public String model;
    }
}
