package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/22.
 */
public class ViewInitRequest extends AbsBaseRequest<ViewInitBean> {
    public ViewInitRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ViewInitRequest(String url, AbsBaseParam model, HttpListener<ViewInitBean> listener) {
        super(url, model, listener);
    }

    public ViewInitRequest(AbsBaseParam model) {
        super(model);
    }

    public ViewInitRequest(AbsBaseParam model, HttpListener<ViewInitBean> listener) {
    super(model, listener);
}
}
