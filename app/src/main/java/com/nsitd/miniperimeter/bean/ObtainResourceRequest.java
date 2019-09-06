package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/24.
 */
public class ObtainResourceRequest extends AbsBaseRequest<ObtainResourceBean>{
    public ObtainResourceRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainResourceRequest(String url, AbsBaseParam model, HttpListener<ObtainResourceBean> listener) {
        super(url, model, listener);
    }

    public ObtainResourceRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainResourceRequest(AbsBaseParam model, HttpListener<ObtainResourceBean> listener) {
        super(model, listener);
    }
}
