package com.nsitd.miniperimeter.util;


import android.util.Log;

import com.nsitd.miniperimeter.BuildConfig;

public class LogFactory {
	public static final boolean DEBUG = BuildConfig.API_ENV;

	public static void i(String tag, String msg) {
		if (DEBUG)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG)
		{
			Log.d(tag, msg);
		}
			
	}

	public static void v(String tag, String msg) {
		if (DEBUG)
			Log.v(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (DEBUG)
			Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
			Log.e(tag, msg);
	}

	public static void print(String msg) {
		if (DEBUG)
			System.out.print(msg);
	}

	public static void println(String msg) {
		if (DEBUG)
			System.out.println(msg);
	}
}