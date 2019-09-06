package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class ModifyPasswordRequest extends AbsBaseRequest<ModifyPasswordBean> {
    public ModifyPasswordRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ModifyPasswordRequest(String url, AbsBaseParam model, HttpListener<ModifyPasswordBean> listener) {
        super(url, model, listener);
    }

    public ModifyPasswordRequest(AbsBaseParam model) {
        super(model);
    }

    public ModifyPasswordRequest(AbsBaseParam model, HttpListener<ModifyPasswordBean> listener) {
        super(model, listener);
    }
}
