package com.nsitd.miniperimeter.bean;

import com.litesuits.http.request.param.HttpParamModel;

/**
 * Created by reimu on 16/3/24.
 */
public abstract class AbsBaseParam<T> implements HttpParamModel{
    public String method;
    public T content;
}
