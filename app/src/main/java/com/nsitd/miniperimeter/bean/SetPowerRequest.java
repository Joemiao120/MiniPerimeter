package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class SetPowerRequest extends AbsBaseRequest<SetPowerBean> {
    public SetPowerRequest(AbsBaseParam model) {
        super(model);
    }

    public SetPowerRequest(AbsBaseParam model, HttpListener<SetPowerBean> listener) {
        super(model, listener);
    }

    public SetPowerRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public SetPowerRequest(String url, AbsBaseParam model, HttpListener<SetPowerBean> listener) {
        super(url, model, listener);
    }
}
