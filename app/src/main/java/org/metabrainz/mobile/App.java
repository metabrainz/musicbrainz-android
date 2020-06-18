package org.metabrainz.mobile;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import org.metabrainz.mobile.presentation.Configuration;
import org.metabrainz.mobile.presentation.UserPreferences;

public class App extends Application {

    public static final int DIRECTORY_SELECT_REQUEST_CODE = 0;
    public static final int AUDIO_FILE_REQUEST_CODE = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final String TAGGER_ROOT_DIRECTORY = Environment.getExternalStorageDirectory() + "/Picard/";
    public static final String WEBSITE_BASE_URL = "https://musicbrainz.org/";
    private static App instance;
    private static Typeface robotoLight;

    public static String getUserAgent() {
        return Configuration.USER_AGENT + "/" + getVersion();
    }

    public static String getClientId() {
        return Configuration.CLIENT_NAME + "-" + getVersion();
    }

    public static String getVersion() {
        try {
            return instance.getPackageManager().getPackageInfo(instance.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "unknown";
        }
    }

    public static App getContext() {
        return instance;
    }

    public static App getInstance() {
        return instance;
    }

    public static Typeface getRobotoLight() {
        return robotoLight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        loadCustomTypefaces();
        if (UserPreferences.getPreferenceListeningEnabled() && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            startListenService();
    }

    private void loadCustomTypefaces() {
        robotoLight = Typeface.createFromAsset(instance.getAssets(), "Roboto-Light.ttf");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startListenService() {
        Intent intent = new Intent(this.getApplicationContext(), ListenService.class);
        startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopListenService() {
        Intent intent = new Intent(this.getApplicationContext(), ListenService.class);
        stopService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationServiceAllowed() {
        String listeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return listeners != null && listeners.contains(getPackageName());
    }

}
