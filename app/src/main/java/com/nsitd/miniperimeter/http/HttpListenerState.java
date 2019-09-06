package com.nsitd.miniperimeter.http;

/**
 * Created by reimu on 16/3/24.
 */
public enum HttpListenerState {
    SERVER_FINISH(1), SERVER_FAILED(0), NETWORK_ERROR(-1),SERVER_CANCEL(-2);

    private int code = 0;

    HttpListenerState(int code) {
        this.code = code;
    }

    public String toString() {
        return String.valueOf(this.code);
    }

    public static HttpListenerState parseFromInteger(int code) {
        if (code == 1) {
            return HttpListenerState.SERVER_FINISH;
        }
        if (code == 0) {
            return HttpListenerState.SERVER_FAILED;
        }

        if(code==-2){
            return HttpListenerState.SERVER_CANCEL;
        }

        return HttpListenerState.NETWORK_ERROR;
    }
}
