package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 23、设置布防/撤防模式
 * Created by reimu on 16/3/24.
 */
public class ObtainDefenseModelRequest extends AbsBaseRequest<ObtainDefenseModelBean>{
    public ObtainDefenseModelRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainDefenseModelRequest(AbsBaseParam model, HttpListener<ObtainDefenseModelBean> listener) {
        super(model, listener);
    }

    public ObtainDefenseModelRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainDefenseModelRequest(String url, AbsBaseParam model, HttpListener<ObtainDefenseModelBean> listener) {
        super(url, model, listener);
    }
}
