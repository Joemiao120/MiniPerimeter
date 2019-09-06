package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/22.
 */
public class ObtainLineRelationRequest extends AbsBaseRequest<ObtainLineRelationBean>{

    public ObtainLineRelationRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public ObtainLineRelationRequest(String url, AbsBaseParam model, HttpListener<ObtainLineRelationBean> listener) {
        super(url, model, listener);
    }

    public ObtainLineRelationRequest(AbsBaseParam model) {
        super(model);
    }

    public ObtainLineRelationRequest(AbsBaseParam model, HttpListener<ObtainLineRelationBean> listener) {
        super(model, listener);
    }
}
