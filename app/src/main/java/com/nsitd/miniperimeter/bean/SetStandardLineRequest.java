package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 19、设置基准线
 * Created by reimu on 16/3/24.
 */
public class SetStandardLineRequest extends AbsBaseRequest<SetStandardLineBean>{
    public SetStandardLineRequest(AbsBaseParam model) {
        super(model);
    }

    public SetStandardLineRequest(AbsBaseParam model, HttpListener<SetStandardLineBean> listener) {
        super(model, listener);
    }

    public SetStandardLineRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public SetStandardLineRequest(String url, AbsBaseParam model, HttpListener<SetStandardLineBean> listener) {
        super(url, model, listener);
    }
}
