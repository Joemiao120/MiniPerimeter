package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * Created by reimu on 16/4/14.
 */
public class DeleteSomedayHistoryAlarmRequest extends AbsBaseRequest<DeleteSomedayHistoryAlarmBean>{
    public DeleteSomedayHistoryAlarmRequest(AbsBaseParam model) {
        super(model);
    }

    public DeleteSomedayHistoryAlarmRequest(AbsBaseParam model, HttpListener<DeleteSomedayHistoryAlarmBean>
            listener) {
        super(model, listener);
    }

    public DeleteSomedayHistoryAlarmRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public DeleteSomedayHistoryAlarmRequest(String url, AbsBaseParam model, HttpListener<DeleteSomedayHistoryAlarmBean> listener) {
        super(url, model, listener);
    }
}
