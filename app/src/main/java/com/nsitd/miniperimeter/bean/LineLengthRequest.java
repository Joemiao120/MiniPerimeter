package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/21.
 */
public class LineLengthRequest extends AbsBaseRequest<LineLengthBean> {
    public LineLengthRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public LineLengthRequest(String url, AbsBaseParam model, HttpListener<LineLengthBean> listener) {
        super(url, model, listener);
    }

    public LineLengthRequest(AbsBaseParam model) {
        super(model);
    }

    public LineLengthRequest(AbsBaseParam model, HttpListener<LineLengthBean> listener) {
        super(model, listener);
    }
}
