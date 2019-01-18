package org.metabrainz.android.util;

import org.metabrainz.android.config.Configuration;

public class Log {

    public static final String TAG = Configuration.TAG;

    public static void e(String message) {
        android.util.Log.e(TAG, message);
    }

    public static void d(String message) {
        android.util.Log.d(TAG, message);
    }

    public static void v(String message) {
        android.util.Log.v(TAG, message);
    }

}
