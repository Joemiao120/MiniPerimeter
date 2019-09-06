package com.nsitd.miniperimeter.bean;

import com.litesuits.http.listener.HttpListener;

/**
 * 17、删除历史告警
 * Created by reimu on 16/3/24.
 */
public class DeleteHistoryAlarmRequest extends AbsBaseRequest<DeleteHistoryAlarmBean>{
    public DeleteHistoryAlarmRequest(AbsBaseParam model) {
        super(model);
    }

    public DeleteHistoryAlarmRequest(AbsBaseParam model, HttpListener<DeleteHistoryAlarmBean> listener) {
        super(model, listener);
    }

    public DeleteHistoryAlarmRequest(String url, AbsBaseParam model) {
        super(url, model);
    }

    public DeleteHistoryAlarmRequest(String url, AbsBaseParam model, HttpListener<DeleteHistoryAlarmBean> listener) {
        super(url, model, listener);
    }
}
