package com.nsitd.miniperimeter.http;

import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.response.Response;
import com.nsitd.miniperimeter.bean.AbsBaseBean;
import com.nsitd.miniperimeter.util.LogFactory;

/**
 * Created by reimu on 16/3/24.
 */
public class HttpListenerWrap<T> extends HttpListener<T> {
    public final String TAG = HttpListenerWrap.this.getClass().getSimpleName();
    private IHttpListenerWrapCallback<T> callback;
    public HttpListenerWrap(IHttpListenerWrapCallback<T> callback){
        this.callback = callback;
    }


    @Override
    public void onStart(AbstractRequest<T> request) {
        super.onStart(request);
        LogFactory.i(TAG, "----onStart------:" + request.toString());
    }

    @Override
    public void onRetry(AbstractRequest<T> request, int max, int times) {
        super.onRetry(request, max, times);
    }

    @Override
    public void onRedirect(AbstractRequest<T> request, int max, int times) {
        super.onRedirect(request, max, times);
    }

    @Override
    public void onLoading(AbstractRequest<T> request, long total, long len) {
        super.onLoading(request, total, len);
    }

    @Override
    public void onCancel(T t, Response<T> response) {
        super.onCancel(t, response);
        String state = null;
        String result = null;
        if(t!=null&&t instanceof AbsBaseBean){
            AbsBaseBean bean = (AbsBaseBean) t;
            state = bean.state;
            result = bean.message;
        }
        callback.finished(HttpListenerState.SERVER_CANCEL,state,result,t);
    }

    @Override
    public void onEnd(Response<T> response) {
        super.onEnd(response);
    }

    @Override
    public void onFailure(HttpException e, Response<T> response) {
        super.onFailure(e, response);
        LogFactory.i(TAG, "----onFailure------response:" + response.toString() + "\n,-------Exception:" + e.toString());
        callback.finished(HttpListenerState.SERVER_FAILED,null,null,null);
    }

    @Override
    public void onSuccess(T t, Response<T> response) {
        super.onSuccess(t, response);

        String state = null;
        String result = null;
        if(t!=null&&t instanceof AbsBaseBean){
            LogFactory.i(TAG, "----onSuccess------:" + t==null?"":t.toString());
            AbsBaseBean bean = (AbsBaseBean) t;
            state = bean.state;
            result = bean.message;
        }
        callback.finished(HttpListenerState.SERVER_FINISH,state,result,t);
    }

    @Override
    public void onUploading(AbstractRequest<T> request, long total, long len) {
        super.onUploading(request, total, len);
        LogFactory.i(TAG, "----onUploading------:total:" + total+",len:"+len);
    }
    public interface IHttpListenerWrapCallback<T> {
        void finished(HttpListenerState requestSate, String resultSate, String message, T t);
    }
}
