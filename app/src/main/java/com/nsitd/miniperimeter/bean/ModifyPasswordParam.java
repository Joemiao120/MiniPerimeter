package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class ModifyPasswordParam extends AbsBaseParam<ModifyPasswordParam.ModifyPasswordCotent>{

    public ModifyPasswordParam(){
        content = new ModifyPasswordCotent();
    }
    public static class ModifyPasswordCotent{
        public String passWord;
        public String newPassWord;
    }
}
