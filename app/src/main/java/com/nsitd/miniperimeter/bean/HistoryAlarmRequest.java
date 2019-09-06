package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/3/22.
 */
public class HistoryAlarmRequest extends AbsBaseRequest<HistoryAlarmBean> {
    public HistoryAlarmRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public HistoryAlarmRequest(String url, AbsBaseParam model, HttpListener<HistoryAlarmBean> listener) {
        super(url, model, listener);
    }

    public HistoryAlarmRequest(AbsBaseParam model) {
        super(model);
    }

    public HistoryAlarmRequest(AbsBaseParam model, HttpListener<HistoryAlarmBean> listener) {
        super(model, listener);
    }
}
