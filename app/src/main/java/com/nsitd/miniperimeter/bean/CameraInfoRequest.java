package com.nsitd.miniperimeter.bean;

import com.litesuits.http.request.JsonAbsRequest;
import com.litesuits.http.request.param.HttpParamModel;

/**
 * Created by reimu on 16/3/22.
 */
public class CameraInfoRequest extends JsonAbsRequest<CameraInfoBean>{
    public CameraInfoRequest(String url, HttpParamModel model) {
        super(url, model);
    }
}
