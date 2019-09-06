package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.JsonAbsRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.nsitd.miniperimeter.util.CommonAttribute;

/**
 * Created by reimu on 16/3/25.
 */
public class AbsBaseRequest<T> extends JsonAbsRequest<T>{

    protected AbsBaseRequest(AbsBaseParam model) {
        super(CommonAttribute.getUrl(model.method), model);
        setMethod(HttpMethods.Post);
    }

    protected AbsBaseRequest(AbsBaseParam model,HttpListener<T> listener) {
        super(CommonAttribute.getUrl(model.method), model);
        setMethod(HttpMethods.Post);
        setHttpListener(listener);
    }

    protected AbsBaseRequest(String url, AbsBaseParam model) {
        super(url, model);
        setMethod(HttpMethods.Post);
    }

    protected AbsBaseRequest(String url, AbsBaseParam model,HttpListener<T> listener) {
        super(url, model);
        setMethod(HttpMethods.Post);
        setHttpListener(listener);
    }
}
