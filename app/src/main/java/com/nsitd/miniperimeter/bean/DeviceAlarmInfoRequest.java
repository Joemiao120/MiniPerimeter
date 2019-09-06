package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/25.
 */
public class DeviceAlarmInfoRequest extends AbsBaseRequest<DeviceAlarmInfoBean>{
    public DeviceAlarmInfoRequest(AbsBaseParam model) {

        super(model);
    }

    public DeviceAlarmInfoRequest(AbsBaseParam model, HttpListener<DeviceAlarmInfoBean> listener) {
        super(model, listener);
    }

    public DeviceAlarmInfoRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public DeviceAlarmInfoRequest(String url, AbsBaseParam model, HttpListener<DeviceAlarmInfoBean> listener) {
        super(url, model, listener);
    }
}
