package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 18、获取基准线
 * Created by reimu on 16/3/24.
 */
public class ObtainStandardLineRequest extends AbsBaseRequest<ObtainStandardLineBean>{
    public ObtainStandardLineRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainStandardLineRequest(AbsBaseParam model, HttpListener<ObtainStandardLineBean> listener) {
        super(model, listener);
    }

    public ObtainStandardLineRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainStandardLineRequest(String url, AbsBaseParam model, HttpListener<ObtainStandardLineBean> listener) {
        super(url, model, listener);
    }
}
