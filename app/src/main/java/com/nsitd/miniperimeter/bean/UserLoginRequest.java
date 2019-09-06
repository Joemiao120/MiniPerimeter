package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class UserLoginRequest extends AbsBaseRequest<UserLoginBean> {

    public UserLoginRequest(AbsBaseParam model) {
        super(model);

    }
    public UserLoginRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public UserLoginRequest(AbsBaseParam model, HttpListener<UserLoginBean> listener) {
        super(model, listener);
    }

    public UserLoginRequest(String url, AbsBaseParam model, HttpListener<UserLoginBean> listener) {
        super(url, model, listener);
    }
}
