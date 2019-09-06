package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/4/20.
 */
public class FnCountInfoRequest extends AbsBaseRequest<FnCountInfoBean>{

    public FnCountInfoRequest(AbsBaseParam model) {
        super(model);
    }

    public FnCountInfoRequest(AbsBaseParam model, HttpListener<FnCountInfoBean> listener) {
        super(model, listener);
    }

    public FnCountInfoRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public FnCountInfoRequest(String url, AbsBaseParam model, HttpListener<FnCountInfoBean> listener) {
        super(url, model, listener);
    }
}
