package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 22、设置布防/撤防模式
 * Created by reimu on 16/3/24.
 */
public class SetDefenseModelRequest extends AbsBaseRequest<SetDefenseModelBean> {

    public SetDefenseModelRequest(AbsBaseParam model) {
        super(model);
    }

    public SetDefenseModelRequest(AbsBaseParam model, HttpListener<SetDefenseModelBean> listener) {
        super(model, listener);
    }

    public SetDefenseModelRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public SetDefenseModelRequest(String url, AbsBaseParam model, HttpListener<SetDefenseModelBean> listener) {
        super(url, model, listener);
    }
}
