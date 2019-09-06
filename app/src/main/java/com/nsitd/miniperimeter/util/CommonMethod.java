package com.nsitd.miniperimeter.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Created by happy on 16/3/18.
 */
public class CommonMethod {

    private static final String TAG = CommonMethod.class.getSimpleName();

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getDecorviewStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 得到显示区域的高度
     *
     * @param context
     * @return
     */
    public static int contentViewHight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int statusBarHeight = getStatusBarHeight(context);
        int viewHight = metric.heightPixels - statusBarHeight;
        LogFactory.i(TAG, "contentViewHight==metric.heightPixels=" + metric.heightPixels + "===" + metric.widthPixels);
        return viewHight;
    }

    /**
     * 得到显示区域的宽度
     *
     * @param context
     * @return
     */
    public static int contentViewWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        int viewWidth = metric.widthPixels;
        LogFactory.i(TAG, "contentViewHight==metric.heightPixels=" + metric.heightPixels + "===" + metric.widthPixels);
        return viewWidth;
    }


    public static String FormatMis(long miss) {
        int hh = (int) (miss / 3600 > 9 ? miss / 3600 : miss / 3600);
        int mm = (int) ((miss % 3600) / 60 > 9 ? (miss % 3600) / 60 : (miss % 3600) / 60);
        mm = mm + hh * 60;
        String mms = (mm > 9) ? mm + "" : "0" + mm;


        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0"
                + (miss % 3600) % 60;
        return mms + ":" + ss;
    }

    public static String FormatMiss(long miss) {
//        miss =  miss/1000;
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0"
                + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0"
                + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    private static Toast toast = null;

    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
