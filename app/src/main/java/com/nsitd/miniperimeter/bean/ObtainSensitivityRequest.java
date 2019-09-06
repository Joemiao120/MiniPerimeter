package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class ObtainSensitivityRequest extends AbsBaseRequest<ObtainSensitivityBean> {
    public ObtainSensitivityRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainSensitivityRequest(AbsBaseParam model, HttpListener<ObtainSensitivityBean> listener) {
        super(model, listener);
    }

    public ObtainSensitivityRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainSensitivityRequest(String url, AbsBaseParam model, HttpListener<ObtainSensitivityBean> listener) {
        super(url, model, listener);
    }
}
