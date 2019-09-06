package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 视频与线关联信息更新
 * Created by reimu on 16/3/24.
 */
public class UpdateLineRelationRequest extends AbsBaseRequest<UpdateLineRelationBean>{
    public UpdateLineRelationRequest(AbsBaseParam model) {
        super(model);
    }

    public UpdateLineRelationRequest(AbsBaseParam model, HttpListener<UpdateLineRelationBean> listener) {
        super(model, listener);
    }

    public UpdateLineRelationRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public UpdateLineRelationRequest(String url, AbsBaseParam model, HttpListener<UpdateLineRelationBean> listener) {
        super(url, model, listener);
    }
}
