package com.nsitd.miniperimeter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.nsitd.miniperimeter.util.AlarmVoicePlayer;
import com.nsitd.miniperimeter.util.CrashHandler;
import com.nsitd.miniperimeter.util.LogFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by reimu on 16/3/25.
 */
public class MyApplication extends Application {
    private static MyApplication application;
    private static Map<String, Activity> runingActivities = new ConcurrentHashMap<String, Activity>();

    @Override
    public void onCreate() {
        application = this;

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        AlarmVoicePlayer alarmVoicePlayer = AlarmVoicePlayer.getInstance();
        alarmVoicePlayer.init(getApplicationContext());

        super.onCreate();
    }

    public static Context getApplication() {
        return application;
    }

    public static void addActivity(Activity activity) {
        if (activity != null)
            runingActivities.put(activity.getClass().getCanonicalName(),
                    activity);
    }

    public static void closeActivity(Class[] clazzs) {
        if (clazzs == null)
            return;
        for (Class clazz : clazzs) {
            closeActivity(clazz);
        }
    }

    public static void closeActivity(Class clazz) {
        if (clazz != null) {
            String name = clazz.getCanonicalName();
            Activity activity = runingActivities.get(name);
            if (activity != null) {
                activity.finish();
                runingActivities.remove(name);
            }
        }
    }

    public static void closeAllNotThis(Class clazz) {
        Set<String> keySet = runingActivities.keySet();
        for (String key : keySet) {
            if (TextUtils.equals(clazz.getCanonicalName(), key)) continue;
            Activity activity = runingActivities.get(key);
            activity.finish();
            runingActivities.remove(key);
        }
    }

    public static void closeAll() {
        Set<String> keySet = runingActivities.keySet();
        for (String key : keySet) {
            Activity activity = runingActivities.get(key);
            activity.finish();
            runingActivities.remove(key);
        }
    }

}
