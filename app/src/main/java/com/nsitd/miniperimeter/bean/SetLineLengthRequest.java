package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class SetLineLengthRequest extends AbsBaseRequest<SetLineLengthBean> {
    public SetLineLengthRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public SetLineLengthRequest(String url, AbsBaseParam model, HttpListener<SetLineLengthBean> listener) {
        super(url, model, listener);
    }

    public SetLineLengthRequest(AbsBaseParam model) {
        super(model);
    }

    public SetLineLengthRequest(AbsBaseParam model, HttpListener<SetLineLengthBean> listener) {
        super(model, listener);
    }
}
