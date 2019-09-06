package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class SetSensitivityRequest extends AbsBaseRequest<SetSensitivityBean>{
    public SetSensitivityRequest(AbsBaseParam model) {
        super(model);
    }

    public SetSensitivityRequest(AbsBaseParam model, HttpListener<SetSensitivityBean> listener) {
        super(model, listener);
    }

    public SetSensitivityRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public SetSensitivityRequest(String url, AbsBaseParam model, HttpListener<SetSensitivityBean> listener) {
        super(url, model, listener);
    }
}
