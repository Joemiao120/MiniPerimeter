package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class ObtainPowerRequest extends AbsBaseRequest<ObtainPowerBean> {
    public ObtainPowerRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainPowerRequest(AbsBaseParam model, HttpListener<ObtainPowerBean> listener) {
        super(model, listener);
    }

    public ObtainPowerRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainPowerRequest(String url, AbsBaseParam model, HttpListener<ObtainPowerBean> listener) {
        super(url, model, listener);
    }
}
