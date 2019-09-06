package com.nsitd.miniperimeter.util;

import com.nsitd.miniperimeter.bean.DateBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by reimu on 16/2/26.
 */
public class CalendarUtil {

    public static int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH);
    }


    public static String getDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return date;
    }

    public static int getCurrentHour() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MINUTE);
    }

    public static int getCurrentSecond() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.SECOND);
    }

    public static DateBean getDate(DateBean date,int type,int offset){
        Calendar cal = Calendar.getInstance();
        cal.set(date.year,date.month,date.day,date.hour,date.minute,date.second);
        cal.add(type,offset);
        DateBean dateBean = new DateBean();
        dateBean.year = cal.get(Calendar.YEAR);
        dateBean.month = cal.get(Calendar.MONTH);
        dateBean.day = cal.get(Calendar.DAY_OF_MONTH);
        dateBean.hour = cal.get(Calendar.HOUR_OF_DAY);
        dateBean.minute = cal.get(Calendar.MINUTE);
        dateBean.second = cal.get(Calendar.SECOND);
        return dateBean;
    }

}
