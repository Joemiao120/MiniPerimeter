package com.nsitd.miniperimeter.bean;

import de.greenrobot.event.EventBus;

/**
 * Created by mac on 16/5/11.
 */
public class EventBusBean {
    private String msg;
    private int number;

    public EventBusBean(){

    }
    public EventBusBean(String msg){
        this.msg = msg;
    }

    public EventBusBean(int number){
        this.number = number;
    }

    public String getMsg() {
        return msg;
    }

    public int getNumber() {
        return number;
    }
}
