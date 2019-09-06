package com.nsitd.miniperimeter.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.nsitd.miniperimeter.MyApplication;

/**
 * SharePreference封装
 * 
 */
public class PrefUtils {

	public static final String PREF_NAME = "config";

	public static boolean getBoolean(String key,
			boolean defaultValue) {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}
	public static boolean getBoolean(Context ctx,String key,
			boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(String key, boolean value) {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static String getString( String key, String defaultValue) {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(String key, String value) {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	
	public static int getInt(String key,
			int defaultValue){
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}
	
	public static void setInt(String key, int value) {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	
	public static void clear() {
		SharedPreferences sp = MyApplication.getApplication().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().clear().commit();

	}
}
