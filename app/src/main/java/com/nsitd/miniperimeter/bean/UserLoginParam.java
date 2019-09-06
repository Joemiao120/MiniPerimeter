package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class UserLoginParam extends AbsBaseParam<UserLoginParam.UserLoginCotent> {

    public UserLoginParam(){
        content = new UserLoginCotent();
    }

    public static class UserLoginCotent{
        public String passWord;
    }
}
