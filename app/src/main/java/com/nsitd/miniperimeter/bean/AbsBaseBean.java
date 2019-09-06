package com.nsitd.miniperimeter.bean;

/**
 * Created by reimu on 16/3/24.
 */
public abstract class AbsBaseBean<T> {
    public String method;
    public String message;
    public String state;
    public T content;

    @Override
    public String toString() {
        if(this==null)return "";
        return "AbsBaseBean{" +
                "method='" + method + '\'' +
                ", message='" + message + '\'' +
                ", state='" + state + '\'' +
                ", content=" + content.toString() +
                '}';
    }
}
