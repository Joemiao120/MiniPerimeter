package com.nsitd.miniperimeter.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by reimu on 16/4/12.
 */
public class DateBean implements Serializable {
    public DateBean() {
    }

    public DateBean(String date) {
        if(TextUtils.isEmpty(date))return;
        String[] ds = date.split(" ");
        if(ds.length<=0)return;
        String[] dates = ds[0].split("-");
        this.year = Integer.parseInt(dates[0]);
        this.month = Integer.parseInt(dates[1]);
        this.day = Integer.parseInt(dates[2]);
        if(ds.length<=1)return;
        String[] times = ds[1].split(":");
        this.hour =  Integer.parseInt(times[0]);
        this.minute = Integer.parseInt(times[1]);
        this.second = Integer.parseInt(times[2]);
    }

    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

    @Override
    public String toString() {
        return "DateBean{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }
}
