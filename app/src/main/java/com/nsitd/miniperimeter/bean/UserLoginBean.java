package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/21.
 */
public class UserLoginBean extends AbsBaseBean<UserLoginBean.UserLogin>{

    public static class UserLogin{
        public String token;

        @Override
        public String toString() {
            return "UserLogin{" +
                    "token='" + token + '\'' +
                    '}';
        }
    }


}
