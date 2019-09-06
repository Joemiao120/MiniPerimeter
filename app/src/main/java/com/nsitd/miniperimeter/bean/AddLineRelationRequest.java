package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 视频与线关联信息保存
 * Created by reimu on 16/3/24.
 */
public class AddLineRelationRequest extends AbsBaseRequest<AddLineRelationBean>{
    public AddLineRelationRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public AddLineRelationRequest(String url, AbsBaseParam model, HttpListener<AddLineRelationBean> listener) {
        super(url, model, listener);
    }

    public AddLineRelationRequest(AbsBaseParam model) {
        super(model);
    }

    public AddLineRelationRequest(AbsBaseParam model, HttpListener<AddLineRelationBean> listener) {
        super(model, listener);
    }
}
