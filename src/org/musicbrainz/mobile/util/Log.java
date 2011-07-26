package org.musicbrainz.mobile.util;

public class Log {
	
	public static final String TAG = Config.TAG;
	
	public static void e(String message) {
		android.util.Log.e(TAG, message);
	}
	
	public static void v(String message) {
		android.util.Log.v(TAG, message);
	}
	
}
