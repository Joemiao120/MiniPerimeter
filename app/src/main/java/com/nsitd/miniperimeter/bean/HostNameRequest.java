package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class HostNameRequest extends AbsBaseRequest<HostNameBean> {

    public HostNameRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public HostNameRequest(String url, AbsBaseParam model, HttpListener<HostNameBean> listener) {
        super(url, model, listener);
    }

    public HostNameRequest(AbsBaseParam model) {
        super(model);
    }

    public HostNameRequest(AbsBaseParam model, HttpListener<HostNameBean> listener) {
        super(model, listener);
    }
}
